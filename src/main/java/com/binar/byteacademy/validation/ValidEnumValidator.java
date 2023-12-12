package com.binar.byteacademy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, CharSequence> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
        validateEnumClass();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String stringValue = value.toString();
        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equals(stringValue)) {
                return true;
            }
        }

        return false;
    }

    private void validateEnumClass() {
        if (enumClass == null || !enumClass.isEnum()) {
            throw new IllegalArgumentException("Invalid enum class provided");
        }
    }
}
