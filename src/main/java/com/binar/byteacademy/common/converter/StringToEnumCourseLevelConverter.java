package com.binar.byteacademy.common.converter;

import com.binar.byteacademy.enumeration.EnumCourseLevel;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumCourseLevelConverter implements Converter<String, EnumCourseLevel> {
    @Override
    public EnumCourseLevel convert(String source) {
        try {
            return EnumCourseLevel.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wrong enum 'course level' value");
        }
    }
}
