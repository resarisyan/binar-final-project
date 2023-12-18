package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.MidtransUtil;
import com.binar.byteacademy.dto.response.PurchaseResponse;
import com.binar.byteacademy.entity.Purchase;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.PurchaseRepository;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CourseRepository courseRepository;
    private final MidtransUtil midtransUtil;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(String courseSlug, Principal connectedUser) {
        try {
            return courseRepository.findFirstBySlugCourse(courseSlug)
                    .filter(course -> purchaseRepository.findByCourse(course)
                            .map(purchases -> purchases.stream()
                                    .noneMatch(purchase -> purchase.getPurchaseStatus().equals(EnumPurchaseStatus.PAID) ||
                                            purchase.getPurchaseStatus().equals(EnumPurchaseStatus.PENDING)))
                            .orElse(true)
                    )
                    .map(course -> {
                        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
                        Integer promo = course.getCoursePromos().stream()
                                .filter(coursePromo -> coursePromo.getPromo().getEndDate().isAfter(LocalDateTime.now()) && coursePromo.getPromo().getStartDate().isBefore(LocalDateTime.now()))
                                .map(coursePromo -> coursePromo.getPromo().getDiscount())
                                .findFirst()
                                .orElse(0);
                        Integer amount = (int) (course.getPrice() + (course.getPrice() * 0.1)) - (int) (course.getPrice() * promo * 0.01);
                        Purchase purchase = Purchase.builder()
                                .course(course)
                                .purchaseStatus(EnumPurchaseStatus.PENDING)
                                .ppn(0.1)
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
                    })
                    .orElseThrow(() -> new RuntimeException("Course not found or already purchased"));
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public void paymentCallback(Map<String, Object> request) {
        try {
            log.info("Payment callback received");
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
                                        }
                                    }
                                    case "cancel", "deny", "expire", "failure" ->
                                            purchase.setPurchaseStatus(EnumPurchaseStatus.FAILURE);
                                    case "pending" -> purchase.setPurchaseStatus(EnumPurchaseStatus.PENDING);
                                    case "settlement", "refund", "chargeback", "partial_refund", "partial_chargeback" -> purchase.setPurchaseStatus(EnumPurchaseStatus.SETTLEMENT);
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
    public Page<PurchaseResponse> getAllPurchaseDetailsForAdmin(Pageable pageable) {
        try {
            Page<Purchase> purchasePage = Optional.of(purchaseRepository.findAll(pageable))
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


    private Map<String, Object> createParameter(Purchase purchase) {
        Map<String, Object> parameter = new HashMap<>();
        Map<String, String> purchaseDetail = new HashMap<>();
        purchaseDetail.put("order_id", purchase.getId().toString());
        purchaseDetail.put("gross_amount", purchase.getAmountPaid().toString());
        parameter.put("transaction_details", purchaseDetail);
        return parameter;
    }
}
