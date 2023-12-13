package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.PromoRequest;
import com.binar.byteacademy.dto.response.PromoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromoService {
    PromoResponse addPromo(PromoRequest request);
    void updatePromo(String promoCode, PromoRequest request);
    void deletePromo(String promoCode);
    Page<PromoResponse> getAllPromo(Pageable pageable);
    PromoResponse getPromoDetail(String promoCode);
}
