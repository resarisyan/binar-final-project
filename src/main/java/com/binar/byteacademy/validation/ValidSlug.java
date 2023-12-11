package com.binar.byteacademy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidSlugValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSlug {
    String message() default "Invalid slug format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

