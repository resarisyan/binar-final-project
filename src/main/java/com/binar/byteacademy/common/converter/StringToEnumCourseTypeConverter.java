package com.binar.byteacademy.common.converter;

import com.binar.byteacademy.enumeration.EnumCourseType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumCourseTypeConverter implements Converter<String, EnumCourseType> {

    @Override
    public EnumCourseType convert(String source) {
        try {
            return EnumCourseType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wrong enum 'course type' value");
        }
    }
}
