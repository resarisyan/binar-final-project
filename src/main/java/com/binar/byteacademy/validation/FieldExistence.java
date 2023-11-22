package com.binar.byteacademy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldExistenceValidator.class)
public @interface FieldExistence {
    String message() default "Field value does not exist in the table";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String tableName();

    String fieldName();

    boolean shouldExist() default true;
}

