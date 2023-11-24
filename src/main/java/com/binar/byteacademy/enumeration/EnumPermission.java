package com.binar.byteacademy.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumPermission {
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    CUSTOMER_READ("student:read"),
    CUSTOMER_CREATE("student:create"),
    CUSTOMER_UPDATE("student:update"),
    CUSTOMER_DELETE("student:delete");
    @Getter
    private final String permission;
}
