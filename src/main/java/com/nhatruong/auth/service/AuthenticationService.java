package com.nhatruong.auth.service;

import com.nhatruong.auth.model.AuthenticationResponse;
import com.nhatruong.auth.model.RegisterRequest;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);
}
