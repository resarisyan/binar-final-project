package com.binar.byteacademy.dto.request;

import com.binar.byteacademy.validation.FieldExistence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @FieldExistence(tableName = "users", fieldName = "username", shouldExist = false, message = "Username already exists")
    private String username;

    @NotBlank
    @Email
    @FieldExistence(tableName = "users", fieldName = "email", shouldExist = false, message = "Email already exists")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 10, max = 14)
    @FieldExistence(tableName = "users", fieldName = "phone_number", shouldExist = false, message = "Phone number already exists")
    private String phoneNumber;
}
