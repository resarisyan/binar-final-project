package com.binar.byteacademy.common.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class CustomKeyGenerator implements KeyGenerator {

    @NotNull
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getName()).append(":");
        key.append(method.getName()).append(":");
        for (Object param : params) {
            key.append(param.toString()).append(":");
        }
        return key.toString();
    }
}

