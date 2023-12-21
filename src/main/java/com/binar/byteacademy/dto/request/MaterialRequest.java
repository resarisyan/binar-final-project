package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.enumeration.EnumMaterialType;
import com.binar.byteacademy.validation.ValidSlug;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequest {
    @NotBlank
    private String materialName;
    @NotNull
    @Digits(integer = 2, fraction = 0)
    private Integer serialNumber;
    @NotBlank
    private String videoLink;
    @NotNull
    @Digits(integer = 2, fraction = 0)
    private Integer materialDuration;
    @ValidSlug
    private String slugMaterial;
    @NotNull
    private EnumMaterialType materialType;
    @NotNull
    private String slugChapter;
}
