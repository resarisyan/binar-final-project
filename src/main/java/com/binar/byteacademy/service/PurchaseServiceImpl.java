package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.dto.request.UpdatePurchaseStatusRequest;
import com.binar.byteacademy.dto.request.purchase.BankTransferPurchaseRequest;
import com.binar.byteacademy.dto.request.purchase.CreditCardPurchaseRequest;
import com.binar.byteacademy.dto.response.*;
import com.binar.byteacademy.entity.*;
import com.binar.byteacademy.entity.compositekey.UserProgressKey;
import com.binar.byteacademy.enumeration.EnumPaymentMethod;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.exception.DataConflictException;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CourseRepository courseRepository;
    private final CreditCardDetailRepository creditCardDetailRepository;
    private final UserProgressRepository userProgressRepository;
    private final PaymentProofRepository paymentProofRepository;
    private final JwtUtil jwtUtil;
    private static final Double PPN_PERCENTAGE = 0.11;

    @Transactional
    @Override
    public PurchaseResponse makeBankTransferPurchase(BankTransferPurchaseRequest request) {
        try {
            User user = jwtUtil.getUser();
            Course course = courseRepository.findFirstBySlugCourseAndCourseStatus(request.getSlugCourse(), EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException("Course Not Found"));
            List<EnumPurchaseStatus> purchaseStatuses = Arrays.asList(EnumPurchaseStatus.WAITING_FOR_PAYMENT, EnumPurchaseStatus.PAID);
            if (purchaseRepository.existsByUserAndCourseAndPurchaseStatusIn(user, course, purchaseStatuses)) {
                throw new DataConflictException("Purchase already exists");
            }
            UserProgressKey userProgressKey = new UserProgressKey();
            userProgressKey.setUserId(user.getId());
            userProgressKey.setCourseId(course.getId());
            if (userProgressRepository.findById(userProgressKey).isEmpty()) {
                UserProgress userProgress = UserProgress.builder()
                        .id(userProgressKey)
                        .user(user)
                        .course(course)
                        .isCompleted(false)
                        .coursePercentage(0)
                        .build();
                userProgressRepository.save(userProgress);
            }
            PaymentProof paymentProof = PaymentProof.builder()
                    .pathPaymentProofImage(null)
                    .build();
            paymentProof = paymentProofRepository.save(paymentProof);
            //TODO: Buat slugCourse
            Purchase purchase = Purchase.builder()
                    .ppn(course.getPrice()*PPN_PERCENTAGE)
                    .amountPaid(course.getPrice())
                    .endPaymentDate(LocalDateTime.now().plusDays(1))
                    .purchaseStatus(EnumPurchaseStatus.WAITING_FOR_PAYMENT)
                    .paymentMethod(EnumPaymentMethod.BANK_TRANSFER)
                    //TODO: Masukan slugCourse
                    .user(user)
                    .course(course)
                    .paymentProof(paymentProof)
                    .build();
            purchase = purchaseRepository.save(purchase);
            return PurchaseResponse.builder()
                    .amountPaid(purchase.getAmountPaid())
                    .ppn(purchase.getPpn())
                    .endPaymentDate(purchase.getEndPaymentDate())
                    .purchaseStatus(purchase.getPurchaseStatus())
                    .paymentMethod(purchase.getPaymentMethod())
                    .createdAt(purchase.getCreatedAt())
                    .course(CourseResponse.builder()
                            .courseName(purchase.getCourse().getCourseName())
                            .instructorName(purchase.getCourse().getInstructorName())
                            .price(purchase.getCourse().getPrice())
                            .totalCourseRate(purchase.getCourse().getTotalCourseRate())
                            .totalModules(purchase.getCourse().getTotalModules())
                            .courseDuration(purchase.getCourse().getCourseDuration())
                            .slugCourse(purchase.getCourse().getSlugCourse())
                            .courseType(purchase.getCourse().getCourseType())
                            .courseLevel(purchase.getCourse().getCourseLevel())
                            .pathCourseImage(purchase.getCourse().getPathCourseImage())
                            .category(CategoryResponse.builder()
                                    .categoryName(purchase.getCourse().getCategory().getCategoryName())
                                    .pathCategoryImage(purchase.getCourse().getCategory().getPathCategoryImage())
                                    .build())
                            .build())
                    .slugPurchase(purchase.getSlugPurchase())
                    .build();
        } catch (DataNotFoundException | DataConflictException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to make purchase using transfer bank");
            throw new ServiceBusinessException("Failed to make purchase using transfer bank");
        }
    }

    @Transactional
    @Override
    public PurchaseResponse makeCreditCardPurchase(CreditCardPurchaseRequest request) {
        try {
            User user = jwtUtil.getUser();
            Course course = courseRepository.findFirstBySlugCourseAndCourseStatus(request.getSlugCourse(), EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException("Course Not Found"));
            List<EnumPurchaseStatus> purchaseStatuses = Arrays.asList(EnumPurchaseStatus.WAITING_FOR_PAYMENT, EnumPurchaseStatus.PAID);
            if (purchaseRepository.existsByUserAndCourseAndPurchaseStatusIn(user, course, purchaseStatuses)) {
                throw new DataConflictException("Purchase already exists");
            }
            UserProgressKey userProgressKey = new UserProgressKey();
            userProgressKey.setUserId(user.getId());
            userProgressKey.setCourseId(course.getId());
            if (userProgressRepository.findById(userProgressKey).isEmpty()) {
                UserProgress userProgress = UserProgress.builder()
                        .id(userProgressKey)
                        .user(user)
                        .course(course)
                        .isCompleted(false)
                        .coursePercentage(0)
                        .build();
                userProgressRepository.save(userProgress);
            }
            PaymentProof paymentProof = PaymentProof.builder()
                    .pathPaymentProofImage(null)
                    .build();
            paymentProof = paymentProofRepository.save(paymentProof);
            //TODO: Buat slugCourse
            Purchase purchase = Purchase.builder()
                    .ppn(course.getPrice()*PPN_PERCENTAGE)
                    .amountPaid(course.getPrice())
                    .endPaymentDate(LocalDateTime.now().plusDays(1))
                    .purchaseStatus(EnumPurchaseStatus.WAITING_FOR_PAYMENT)
                    .paymentMethod(EnumPaymentMethod.CREDIT_CARD)
                    //TODO: Masukan slugCourse
                    .user(user)
                    .course(course)
                    .paymentProof(paymentProof)
                    .build();
            purchase = purchaseRepository.save(purchase);
            CreditCardDetail creditCardDetail = CreditCardDetail.builder()
                    .cardHolderName(request.getCardHolderName())
                    .cardNumber(request.getCardNumber())
                    .cvv(request.getCvv())
                    .expiryDate(request.getExpiryDate())
                    .purchase(purchase)
                    .build();
            creditCardDetailRepository.save(creditCardDetail);
            return PurchaseResponse.builder()
                    .amountPaid(purchase.getAmountPaid())
                    .ppn(purchase.getPpn())
                    .endPaymentDate(purchase.getEndPaymentDate())
                    .purchaseStatus(purchase.getPurchaseStatus())
                    .paymentMethod(purchase.getPaymentMethod())
                    .createdAt(purchase.getCreatedAt())
                    .course(CourseResponse.builder()
                            .courseName(purchase.getCourse().getCourseName())
                            .instructorName(purchase.getCourse().getInstructorName())
                            .price(purchase.getCourse().getPrice())
                            .totalCourseRate(purchase.getCourse().getTotalCourseRate())
                            .totalModules(purchase.getCourse().getTotalModules())
                            .courseDuration(purchase.getCourse().getCourseDuration())
                            .slugCourse(purchase.getCourse().getSlugCourse())
                            .courseType(purchase.getCourse().getCourseType())
                            .courseLevel(purchase.getCourse().getCourseLevel())
                            .pathCourseImage(purchase.getCourse().getPathCourseImage())
                            .category(CategoryResponse.builder()
                                    .categoryName(purchase.getCourse().getCategory().getCategoryName())
                                    .pathCategoryImage(purchase.getCourse().getCategory().getPathCategoryImage())
                                    .build())
                            .build())
                    .slugPurchase(purchase.getSlugPurchase())
                    .build();
        } catch (DataNotFoundException | DataConflictException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Failed to make purchase using credit card");
            throw new ServiceBusinessException("Failed to make purchase using credit card");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PurchaseDetailResponse> getAllPurchaseDetailsForCustomer(Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<Purchase> purchasePage = Optional.of(purchaseRepository.findAllByUsernameOrderByFirstPurchaseStatus(
                            user.getUsername(),
                            EnumPurchaseStatus.WAITING_FOR_PAYMENT,
                            pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new  DataNotFoundException("Purchase details not found"));
            return purchasePage.map(purchase -> PurchaseDetailResponse.builder()
                    .amountPaid(purchase.getAmountPaid())
                    .ppn(purchase.getPpn())
                    .endPaymentDate(purchase.getEndPaymentDate())
                    .purchaseStatus(purchase.getPurchaseStatus())
                    .paymentMethod(purchase.getPaymentMethod())
                    .createdAt(purchase.getCreatedAt())
                    .course(CourseResponse.builder()
                            .courseName(purchase.getCourse().getCourseName())
                            .instructorName(purchase.getCourse().getInstructorName())
                            .price(purchase.getCourse().getPrice())
                            .totalCourseRate(purchase.getCourse().getTotalCourseRate())
                            .totalModules(purchase.getCourse().getTotalModules())
                            .courseDuration(purchase.getCourse().getCourseDuration())
                            .slugCourse(purchase.getCourse().getSlugCourse())
                            .courseType(purchase.getCourse().getCourseType())
                            .courseLevel(purchase.getCourse().getCourseLevel())
                            .pathCourseImage(purchase.getCourse().getPathCourseImage())
                            .category(CategoryResponse.builder()
                                    .categoryName(purchase.getCourse().getCategory().getCategoryName())
                                    .pathCategoryImage(purchase.getCourse().getCategory().getPathCategoryImage())
                                    .build())
                            .build())
                    .paymentProofResponse(Optional.ofNullable(purchase.getPaymentProof())
                            .map(paymentProof -> PaymentProofResponse.builder()
                                    .pathPaymentProofImage(paymentProof.getPathPaymentProofImage())
                                    .build())
                            .orElse(null))
                    .slugPurchase(purchase.getSlugPurchase())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get purchase details");
            throw new ServiceBusinessException("Failed to get purchase details");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminPurchaseDetailResponse> getAllPurchaseDetailsForAdmin(Pageable pageable) {
        try {
            Page<Purchase> purchasePage = Optional.of(purchaseRepository.findAllOrderByFirstPurchaseStatus(
                            EnumPurchaseStatus.WAITING_FOR_PAYMENT,
                            pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new  DataNotFoundException("Purchase details not found"));
            return purchasePage.map(purchase -> AdminPurchaseDetailResponse.builder()
                    .username(purchase.getUser().getUsername())
                    .amountPaid(purchase.getAmountPaid())
                    .ppn(purchase.getPpn())
                    .endPaymentDate(purchase.getEndPaymentDate())
                    .purchaseStatus(purchase.getPurchaseStatus())
                    .paymentMethod(purchase.getPaymentMethod())
                    .createdAt(purchase.getCreatedAt())
                    .course(CourseResponse.builder()
                            .courseName(purchase.getCourse().getCourseName())
                            .instructorName(purchase.getCourse().getInstructorName())
                            .price(purchase.getCourse().getPrice())
                            .totalCourseRate(purchase.getCourse().getTotalCourseRate())
                            .totalModules(purchase.getCourse().getTotalModules())
                            .courseDuration(purchase.getCourse().getCourseDuration())
                            .slugCourse(purchase.getCourse().getSlugCourse())
                            .courseType(purchase.getCourse().getCourseType())
                            .courseLevel(purchase.getCourse().getCourseLevel())
                            .pathCourseImage(purchase.getCourse().getPathCourseImage())
                            .category(CategoryResponse.builder()
                                    .categoryName(purchase.getCourse().getCategory().getCategoryName())
                                    .pathCategoryImage(purchase.getCourse().getCategory().getPathCategoryImage())
                                    .build())
                            .build())
                    .paymentProofResponse(Optional.ofNullable(purchase.getPaymentProof())
                            .map(paymentProof -> PaymentProofResponse.builder()
                                    .pathPaymentProofImage(paymentProof.getPathPaymentProofImage())
                                    .build())
                            .orElse(null))
                    .slugPurchase(purchase.getSlugPurchase())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get purchase details for admin");
            throw new ServiceBusinessException("Failed to get purchase details for admin");
        }
    }

    @Transactional
    @Override
    public void updatePurchaseStatus(UpdatePurchaseStatusRequest request) {
        try {
            Purchase purchase = purchaseRepository.findFirstBySlugPurchase(request.getSlugPurchase())
                    .orElseThrow(() -> new DataNotFoundException("Purchase not found"));
            purchase.setPurchaseStatus(request.getPurchaseStatus());
            purchaseRepository.save(purchase);
        } catch (DataNotFoundException e) {
            throw e;
        }  catch (Exception e) {
            log.error("Failed to get purchase details for admin");
            throw new ServiceBusinessException("Failed to get purchase details for admin");
        }
    }
}
