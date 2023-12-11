package com.binar.byteacademy.validation;


import com.binar.byteacademy.exception.ServiceBusinessException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class FieldExistenceValidator implements ConstraintValidator<FieldExistence, Object> {
    private final JdbcTemplate jdbcTemplate;
    private String tableName;
    private String fieldName;
    private boolean shouldExist;

    @Override
    public void initialize(FieldExistence constraint) {
        this.tableName = constraint.tableName();
        this.fieldName = constraint.fieldName();
        this.shouldExist = constraint.shouldExist();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + fieldName + "=" + "?";
            int count = jdbcTemplate.queryForObject(sql, Integer.class, value);
            return shouldExist ? count > 0 : count == 0;
        } catch (Exception e) {
            log.error("Failed to check field exists");
            throw new ServiceBusinessException("Failed to check field exists");
        }
    }
}

