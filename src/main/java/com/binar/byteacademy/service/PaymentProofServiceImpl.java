package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.ImageUtil;
import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.dto.request.UpdatePaymentProofRequest;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.PaymentProof;
import com.binar.byteacademy.entity.Purchase;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.PaymentProofRepository;
import com.binar.byteacademy.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentProofServiceImpl implements PaymentProofService {
    private final PaymentProofRepository paymentProofRepository;
    private final PurchaseRepository purchaseRepository;
    private final CourseRepository courseRepository;
    private final ImageUtil imageUtil;
    private final JwtUtil jwtUtil;

    @Override
    public void updatePaymentProof(String slugCourse, UpdatePaymentProofRequest request) {
        try {
            User user = jwtUtil.getUser();
            Course course = courseRepository.findFirstBySlugCourse(slugCourse)
                    .orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));
            Purchase purchase = purchaseRepository.findByUserAndCourse(user, course)
                    .orElseThrow(() -> new DataNotFoundException(PURCHASE_NOT_FOUND));
            PaymentProof paymentProof = purchase.getPaymentProof();
            String pathPaymentProofImage = imageUtil.base64UploadImage(request.getPathPaymentProofImage()).join();
            paymentProof.setPathPaymentProofImage(pathPaymentProofImage);
            paymentProofRepository.save(paymentProof);
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Failed to update payment proof");
            throw new ServiceBusinessException("Failed to update payment proof");
        }
    }
}
