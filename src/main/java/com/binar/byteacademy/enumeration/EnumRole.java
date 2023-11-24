package com.binar.byteacademy.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.binar.byteacademy.enumeration.EnumPermission.*;

@RequiredArgsConstructor
public enum EnumRole {
    CUSTOMER(
            Set.of(
                    CUSTOMER_CREATE,
                    CUSTOMER_READ,
                    CUSTOMER_UPDATE,
                    CUSTOMER_DELETE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    );

    @Getter
    private final Set<EnumPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
