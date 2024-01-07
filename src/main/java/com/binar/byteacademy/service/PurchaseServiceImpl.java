package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.MidtransUtil;
import com.binar.byteacademy.dto.request.NotificationRequest;
import com.binar.byteacademy.dto.response.AdminPurchaseResponse;
import com.binar.byteacademy.dto.response.PurchaseResponse;
import com.binar.byteacademy.entity.Purchase;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.entity.UserProgress;
import com.binar.byteacademy.entity.compositekey.UserProgressKey;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.*;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.PURCHASE_NOT_FOUND;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "purchases")
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CourseRepository courseRepository;
    private final MidtransUtil midtransUtil;
    private final UserProgressRepository userProgressRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    @CacheEvict(value = "allPurchases", allEntries = true, condition = "#result != null")
    public PurchaseResponse createPurchase(String courseSlug, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

            return courseRepository.findFirstBySlugCourse(courseSlug)
                    .filter(course -> {
                                boolean isNotPurchasedOrPending = purchaseRepository.findByCourseAndUser(course, user)
                                        .map(purchase -> purchase.getPurchaseStatus().equals(EnumPurchaseStatus.PAID) || purchase.getPurchaseStatus().equals(EnumPurchaseStatus.PENDING))
                                        .orElse(false);
                                return !isNotPurchasedOrPending;
                            }
                    )
                    .map(course -> {
                        double ppn = course.getCourseType() == EnumCourseType.FREE ? 0 : 0.1;
                        if (course.getCourseType() == EnumCourseType.FREE) {
                            Purchase purchase = Purchase.builder()
                                    .course(course)
                                    .purchaseStatus(EnumPurchaseStatus.PAID)
                                    .ppn(ppn)
                                    .amountPaid(0)
                                    .user(user)
                                    .build();
                            Purchase savedPurchase = purchaseRepository.save(purchase);
                            purchaseSuccess(purchase);
                            notificationPurchaseSuccess(user.getUsername(), course.getCourseName());
                            return PurchaseResponse.builder()
                                    .courseName(savedPurchase.getCourse().getCourseName())
                                    .amountPaid(savedPurchase.getAmountPaid())
                                    .ppn(savedPurchase.getPpn())
                                    .purchaseDate(savedPurchase.getPurchaseDate())
                                    .purchaseStatus(savedPurchase.getPurchaseStatus())
                                    .tokenPurchase(savedPurchase.getTokenPurchase())
                                    .build();
                        } else {
                            Integer promo = course.getCoursePromos().stream()
                                    .filter(coursePromo -> coursePromo.getPromo().getEndDate().isAfter(LocalDateTime.now()) && coursePromo.getPromo().getStartDate().isBefore(LocalDateTime.now()))
                                    .map(coursePromo -> coursePromo.getPromo().getDiscount())
                                    .findFirst()
                                    .orElse(0);
                            Integer amount = (int) (course.getPrice() + (course.getPrice() * 0.1)) - (int) (course.getPrice() * promo * 0.01);
                            Purchase purchase = Purchase.builder()
                                    .course(course)
                                    .purchaseStatus(EnumPurchaseStatus.PENDING)
                                    .ppn(ppn)
                                    .amountPaid(amount)
                                    .user(user)
                                    .build();
                            Purchase savedPurchase = purchaseRepository.save(purchase);
                            Map<String, Object> parameter = createParameter(savedPurchase);
                            String transactionToken;

                            try {
                                transactionToken = midtransUtil.getSnapApi().createTransactionToken(parameter);
                                purchase.setTokenPurchase(transactionToken);
                                purchaseRepository.save(purchase);
                            } catch (MidtransError e) {
                                throw new ServiceBusinessException(e.getMessage());
                            }

                            return PurchaseResponse.builder()
                                    .courseName(savedPurchase.getCourse().getCourseName())
                                    .amountPaid(savedPurchase.getAmountPaid())
                                    .ppn(savedPurchase.getPpn())
                                    .purchaseDate(savedPurchase.getPurchaseDate())
                                    .purchaseStatus(savedPurchase.getPurchaseStatus())
                                    .tokenPurchase(transactionToken)
                                    .build();
                        }
                    })
                    .orElseThrow(() -> new DataNotFoundException("Course not found or already purchased"));
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to create purchase");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "allPurchases", allEntries = true)
    public void paymentCallback(Map<String, Object> request) {
        try {
            String orderId = (String) Optional.ofNullable(request.get("order_id"))
                    .orElseThrow(() -> new ServiceBusinessException("Order id is required"));
            JSONObject transactionResult = midtransUtil.getCoreApi().checkTransaction(orderId);
            String transactionStatus = (String) transactionResult.get("transaction_status");
            String fraudStatus = (String) transactionResult.get("fraud_status");
            purchaseRepository.findById(UUID.fromString(orderId))
                    .ifPresentOrElse(
                            purchase -> {
                                switch (transactionStatus) {
                                    case "capture" -> {
                                        if (fraudStatus.equals("challenge")) {
                                            purchase.setPurchaseStatus(EnumPurchaseStatus.CHALLENGE);
                                        } else if (fraudStatus.equals("accept")) {
                                            purchase.setPurchaseStatus(EnumPurchaseStatus.PAID);
                                            purchaseSuccess(purchase);
                                            notificationPurchaseSuccess(purchase.getUser().getUsername(), purchase.getCourse().getCourseName());
                                        }
                                    }
                                    case "cancel", "deny", "expire", "failure" ->
                                            purchase.setPurchaseStatus(EnumPurchaseStatus.FAILURE);
                                    case "pending" -> purchase.setPurchaseStatus(EnumPurchaseStatus.PENDING);
                                    case "settlement" -> {
                                        purchase.setPurchaseStatus(EnumPurchaseStatus.PAID);
                                        purchaseSuccess(purchase);
                                        notificationPurchaseSuccess(purchase.getUser().getUsername(), purchase.getCourse().getCourseName());
                                    }
                                    case "refund", "chargeback", "partial_refund", "partial_chargeback" ->
                                            purchase.setPurchaseStatus(EnumPurchaseStatus.REFUND);
                                    default -> throw new ServiceBusinessException("Transaction status not found");
                                }
                                purchaseRepository.save(purchase);
                            },
                            () -> {
                                throw new DataNotFoundException(PURCHASE_NOT_FOUND);
                            }
                    );
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to update purchase status");
        }
    }

    @Override
    @Cacheable(value = "allPurchases", key = "'getAllPurchaseDetailsForCustomer-' + #pageable.pageNumber + '-' + #pageable.pageSize", unless = "#result == null")
    public Page<PurchaseResponse> getAllPurchaseDetailsForCustomer(Pageable pageable, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            Page<Purchase> purchasePage = Optional.ofNullable(purchaseRepository.findByUserOrderByCreatedAtDesc(user, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(PURCHASE_NOT_FOUND));
            return purchasePage.map(purchase -> PurchaseResponse.builder()
                    .courseName(purchase.getCourse().getCourseName())
                    .amountPaid(purchase.getAmountPaid())
                    .purchaseDate(purchase.getPurchaseDate())
                    .purchaseStatus(purchase.getPurchaseStatus())
                    .tokenPurchase(purchase.getTokenPurchase())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get purchase details");
        }
    }

    @Override
    @Cacheable(value = "allPurchases", key = "'getAllPurchaseDetailsForAdmin-' + #pageable.pageNumber + '-' + #pageable.pageSize", unless = "#result == null")
    public Page<AdminPurchaseResponse> getAllPurchaseDetailsForAdmin(Pageable pageable) {
        try {
            Page<Purchase> purchasePage = Optional.of(purchaseRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(PURCHASE_NOT_FOUND));
            return purchasePage.map(purchase -> AdminPurchaseResponse.builder()
                    .courseName(purchase.getCourse().getCourseName())
                    .amountPaid(purchase.getAmountPaid())
                    .purchaseDate(purchase.getPurchaseDate())
                    .purchaseStatus(purchase.getPurchaseStatus())
                    .tokenPurchase(purchase.getTokenPurchase())
                    .username(purchase.getUser().getUsername())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get purchase details");
        }
    }

    private void purchaseSuccess(Purchase purchase) {
        userProgressRepository.save(UserProgress.builder()
                .id(UserProgressKey.builder()
                        .userId(purchase.getUser().getId())
                        .courseId(purchase.getCourse().getId())
                        .build())
                .user(purchase.getUser())
                .course(purchase.getCourse())
                .isCompleted(false)
                .coursePercentage(0)
                .build());
    }

    private void notificationPurchaseSuccess(String username, String courseName) {
        notificationService.sendToUser(NotificationRequest.builder()
                .title("Payment Success")
                .body("Your payment for " + courseName + " is success")
                .build(), username);
    }


    private Map<String, Object> createParameter(Purchase purchase) {
        Map<String, Object> parameter = new HashMap<>();
        Map<String, String> purchaseDetail = new HashMap<>();
        purchaseDetail.put("order_id", purchase.getId().toString());
        purchaseDetail.put("gross_amount", purchase.getAmountPaid().toString());
        parameter.put("transaction_details", purchaseDetail);
        return parameter;
    }
}
