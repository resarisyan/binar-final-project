package com.binar.byteacademy.common.util;

import com.binar.byteacademy.exception.ServiceBusinessException;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlugUtil {
    private final JdbcTemplate jdbcTemplate;

    public String toSlug(String tableName, String fieldName, String input) {
        try {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be null or empty");
            }
            final Slugify slg = Slugify.builder().build();
            String slug = slg.slugify(input);
            boolean checkSlug = isSlugExist(tableName, fieldName, slug);
            if (checkSlug) {
                slug = slug + "-" + System.currentTimeMillis();
            }
            return slug;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    private boolean isSlugExist(String tableName, String fieldName, String value) {
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + fieldName + "=" + "?";
            int count = jdbcTemplate.queryForObject(sql, Integer.class, value);
            return count > 0;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to check field exists");
        }
    }
}
