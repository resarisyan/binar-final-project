package com.binar.byteacademy.validation;

import com.github.slugify.Slugify;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidSlugValidator implements ConstraintValidator<ValidSlug, String> {

    private static final String SLUG_PATTERN = "^[a-z0-9-]+$";
    private static final Pattern pattern = Pattern.compile(SLUG_PATTERN);

    @Override
    public void initialize(ValidSlug constraintAnnotation) {
        // Auto-generated method stub
    }

    @Override
    public boolean isValid(String slug, ConstraintValidatorContext context) {
        return slug == null || slug.isEmpty() || (pattern.matcher(slug).matches() && isClean(slug));
    }

    private boolean isClean(String slug) {
        final Slugify slg = Slugify.builder().build();
        final String result = slg.slugify(slug);
        return result.equals(slug);
    }
}

