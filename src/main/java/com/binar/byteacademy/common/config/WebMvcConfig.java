package com.binar.byteacademy.common.config;

import com.binar.byteacademy.common.converter.StringToEnumCourseLevelConverter;
import com.binar.byteacademy.common.converter.StringToEnumCourseTypeConverter;
import com.binar.byteacademy.common.converter.StringToEnumFilterCoursesByConverter;
import com.binar.byteacademy.common.converter.StringToEnumStatusConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumCourseLevelConverter());
        registry.addConverter(new StringToEnumCourseTypeConverter());
        registry.addConverter(new StringToEnumStatusConverter());
        registry.addConverter(new StringToEnumFilterCoursesByConverter());
    }
}
