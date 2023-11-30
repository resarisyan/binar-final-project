package com.binar.byteacademy.common.converter;

import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumFilterCoursesByConverter implements Converter<String, EnumFilterCoursesBy> {
    @Override
    public EnumFilterCoursesBy convert(String source) {
        try {
            return EnumFilterCoursesBy.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
