package com.binar.byteacademy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Base64ImageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64Image {
    String message() default "Invalid base64 image format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
