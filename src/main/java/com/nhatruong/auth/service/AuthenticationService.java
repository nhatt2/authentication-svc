package com.nhatruong.auth.service;

import com.nhatruong.auth.model.AuthenticationRequest;
import com.nhatruong.auth.model.AuthenticationResponse;
import com.nhatruong.auth.model.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);
    
    public AuthenticationResponse authenticate(AuthenticationRequest request);
    
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
