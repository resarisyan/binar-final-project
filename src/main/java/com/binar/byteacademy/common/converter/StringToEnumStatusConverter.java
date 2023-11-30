package com.binar.byteacademy.common.converter;

import com.binar.byteacademy.enumeration.EnumStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumStatusConverter implements Converter<String, EnumStatus> {
    @Override
    public EnumStatus convert(String source) {
        try {
            return EnumStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wrong enum 'status' value");
        }
    }
}
