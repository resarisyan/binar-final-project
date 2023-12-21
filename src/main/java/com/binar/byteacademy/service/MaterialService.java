package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.MaterialRequest;
import com.binar.byteacademy.dto.response.MaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface MaterialService {
    MaterialResponse addMaterial(MaterialRequest request);
    void updateMaterial(String slugMaterial, MaterialRequest request);
    void deleteMaterial(String slugMaterial);
    Page<MaterialResponse> getAllMaterial(Pageable pageable);
    MaterialResponse getMaterialDetailAdmin(String slugMaterial);
    MaterialResponse getMaterialDetailCustomer(String slugMaterial, Principal principal);
}
