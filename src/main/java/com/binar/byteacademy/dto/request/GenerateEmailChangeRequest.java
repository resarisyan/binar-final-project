package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.validation.FieldExistence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateEmailChangeRequest {
    @NotBlank
    @Email
    @FieldExistence(tableName = "users", fieldName = "email", shouldExist = false, message = "Email already exists")
    private String email;
}
