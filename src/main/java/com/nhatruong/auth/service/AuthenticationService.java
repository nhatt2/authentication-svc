package com.nhatruong.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);
}
