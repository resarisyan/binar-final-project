package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.MaterialRequest;
import com.binar.byteacademy.dto.response.MaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialService {
    MaterialResponse addMaterial(MaterialRequest request);
    void updateMaterial(String slugMaterial, MaterialRequest request);
    void deleteMaterial(String slugMaterial);
    Page<MaterialResponse> getAllMaterial(Pageable pageable);
    MaterialResponse getMaterialDetail(String slugMaterial);
}
