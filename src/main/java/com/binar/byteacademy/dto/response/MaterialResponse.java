package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumMaterialType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MaterialResponse {
    private UUID materialId;
    private Integer serialNumber;
    private String videoLink;
    private Integer materialDuration;
    private String slugMaterial;
    private EnumMaterialType materialType;
}
