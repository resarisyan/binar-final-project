package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.dto.request.PromoRequest;
import com.binar.byteacademy.dto.response.PromoResponse;
import com.binar.byteacademy.entity.Promo;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.exception.ValidationException;
import com.binar.byteacademy.repository.PromoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.PROMO_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.TableName.PROMO_TABLE;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "promos")
public class PromoServiceImpl implements PromoService {
    private final PromoRepository promoRepository;
    private final CheckDataUtil checkDataUtil;
    @Override
    @CacheEvict(value = "allPromos", allEntries = true)
    public PromoResponse addPromo(PromoRequest request) {
        try {
            promoRepository.findByPromoCode(request.getPromoCode())
                    .ifPresentOrElse(promo -> {
                        throw new ValidationException("Promo code already exist");
                    }, () -> {
                        Promo promo = Promo.builder()
                                .promoName(request.getPromoName())
                                .promoCode(request.getPromoCode())
                                .discount(request.getDiscount())
                                .promoDescription(request.getPromoDescription())
                                .startDate(request.getStartDate())
                                .endDate(request.getEndDate())
                                .build();
                        promoRepository.save(promo);
                    });

            return PromoResponse.builder()
                    .promoName(request.getPromoName())
                    .promoCode(request.getPromoCode())
                    .discount(request.getDiscount())
                    .promoDescription(request.getPromoDescription())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .build();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to add promo");
        }
    }

    @Override
    @CachePut(key = "'getPromoDetail-' + #promoCode")
    @CacheEvict(value = {"allPromos", "allCoursePromos", "coursePromos"}, allEntries = true)
    public void updatePromo(String promoCode, PromoRequest request) {
        try {
            promoRepository.findByPromoCode(promoCode)
                    .ifPresentOrElse(promo -> {
                        checkDataUtil.checkDataField(PROMO_TABLE, "promo_code", request.getPromoCode(), "promo_id", promo.getId());
                        promo.setPromoName(request.getPromoName());
                        promo.setPromoCode(request.getPromoCode());
                        promo.setDiscount(request.getDiscount());
                        promo.setPromoDescription(request.getPromoDescription());
                        promo.setStartDate(request.getStartDate());
                        promo.setEndDate(request.getEndDate());
                        promoRepository.save(promo);
                    }, () -> {
                        throw new DataNotFoundException(PROMO_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to update promo");
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'getPromoDetail-' + #promoCode"),
            @CacheEvict(value = "allPromos", allEntries = true)
    })
    public void deletePromo(String promoCode) {
        try {
            promoRepository.findByPromoCode(promoCode)
                    .ifPresentOrElse(promoRepository::delete, () -> {
                        throw new ServiceBusinessException(PROMO_NOT_FOUND);
                    });
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete promo");
        }
    }

    @Override
    @Cacheable(value = "allPromos", key = "'getAllPromo-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<PromoResponse> getAllPromo(Pageable pageable) {
        try {
            Page<Promo> promoPage = Optional.of(promoRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(PROMO_NOT_FOUND));
            return promoPage.map(promo -> PromoResponse.builder()
                    .promoName(promo.getPromoName())
                    .promoCode(promo.getPromoCode())
                    .discount(promo.getDiscount())
                    .promoDescription(promo.getPromoDescription())
                    .startDate(promo.getStartDate())
                    .endDate(promo.getEndDate())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get all promo");
        }
    }

    @Override
    @Cacheable(key = "'getPromoDetail-' + #promoCode")
    public PromoResponse getPromoDetail(String promoCode) {
        try {
            return promoRepository.findByPromoCode(promoCode).map(promo -> PromoResponse.builder()
                    .promoName(promo.getPromoName())
                    .promoCode(promo.getPromoCode())
                    .discount(promo.getDiscount())
                    .promoDescription(promo.getPromoDescription())
                    .startDate(promo.getStartDate())
                    .endDate(promo.getEndDate())
                    .build()).orElseThrow(() -> new DataNotFoundException(PROMO_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get promo detail");
        }
    }

}
