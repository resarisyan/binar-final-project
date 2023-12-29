package com.binar.byteacademy.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromoResponse implements Serializable {
    private String promoName;
    private String promoDescription;
    private String promoCode;
    private Integer discount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
