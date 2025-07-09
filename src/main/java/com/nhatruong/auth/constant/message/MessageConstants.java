package com.nhatruong.auth.constant.message;

public final class MessageConstants {
    
    private MessageConstants() {
    }
    
    public static final String USER_NOT_FOUND_MESSAGE = "User not found with email: %s";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";
    public static final String TOKEN_EXPIRED_MESSAGE = "Token has expired";
    public static final String TOKEN_INVALID_MESSAGE = "Invalid token";
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String TOKEN_REFRESHED_SUCCESS = "Token refreshed successfully";
} 