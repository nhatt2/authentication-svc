package com.nhatruong.auth.constant.api;

public final class ApiConstants {
    
    private ApiConstants() {
    }
    
    public static final String[] WHITE_LIST_URLS = {
        "/api/v1/auth/**",
        "/api/v1/auth/register",
        "/api/v1/auth/authenticate",
        "/api/v1/auth/refresh-token"
    };
    
    public static final String AUTH_BASE_URL = "/api/v1/auth";
    public static final String ADMIN_BASE_URL = "/api/v1/admin";
    public static final String EMPLOYEE_BASE_URL = "/api/v1/employee";
    public static final String DEMO_BASE_URL = "/api/v1/demo-controller";
} 