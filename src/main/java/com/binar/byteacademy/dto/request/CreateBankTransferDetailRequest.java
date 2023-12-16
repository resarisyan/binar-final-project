package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.validation.FieldExistence;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBankTransferDetailRequest {
    @NotBlank
    @FieldExistence(tableName = "bank_transfer_details", fieldName = "bank_name", shouldExist = false, message = "Bank name already exists")
    private String bankName;

    @NotBlank
    private String accountNumber;
}
