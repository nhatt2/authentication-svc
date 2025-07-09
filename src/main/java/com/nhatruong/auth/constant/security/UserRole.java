package com.nhatruong.auth.constant.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nhatruong.auth.constant.security.UserPermission.*;

@RequiredArgsConstructor
public enum UserRole {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    EMPLOYEE_READ
            )
    ),
    EMPLOYEE(
            Set.of(
                    EMPLOYEE_READ
            )
    ),
    NORMAL_USER(Set.of());

    @Getter
    private final Set<UserPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
} 