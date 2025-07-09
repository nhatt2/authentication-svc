package com.nhatruong.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomAuthenticationException extends RuntimeException {
    
    public CustomAuthenticationException(String message) {
        super(message);
    }
    
    public CustomAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 