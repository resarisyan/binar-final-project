package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumMaterialType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialResponse implements Serializable {
    private String materialName;
    private Integer serialNumber;
    private String videoLink;
    private Integer materialDuration;
    private String slugMaterial;
    private EnumMaterialType materialType;
}
