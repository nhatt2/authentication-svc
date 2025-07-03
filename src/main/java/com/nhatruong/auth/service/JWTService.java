package com.nhatruong.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JWTService {
    String extractUserName(String jwt);
    public boolean isTokenValid(String token, UserDetails userDetails);
}
