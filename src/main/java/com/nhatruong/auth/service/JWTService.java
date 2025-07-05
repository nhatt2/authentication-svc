package com.nhatruong.auth.service;

import com.nhatruong.auth.dto.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JWTService {
    String extractUserName(String jwt);

    public String generateRefreshToken(UserDetails userDetails);

    public String generateToken(UserDetails userDetails);

    public boolean isTokenValid(String token, UserDetails userDetails);
}
