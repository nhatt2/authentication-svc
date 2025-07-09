package com.nhatruong.auth.controller;

import com.nhatruong.auth.constant.api.ApiConstants;
import com.nhatruong.auth.constant.message.MessageConstants;
import com.nhatruong.auth.dto.response.ApiResponse;
import com.nhatruong.auth.model.AuthenticationRequest;
import com.nhatruong.auth.model.AuthenticationResponse;
import com.nhatruong.auth.model.RegisterRequest;
import com.nhatruong.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(ApiConstants.AUTH_BASE_URL)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request
    ) {
        log.info("Method: register() - User registration request for email: {}", request.getEmail());
        AuthenticationResponse response = service.register(request);
        log.info("Method: register() - User registered successfully with email: {}", request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(MessageConstants.USER_REGISTERED_SUCCESS, response));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        log.info("Method: authenticate() - Authentication request for email: {}", request.getEmail());
        AuthenticationResponse response = service.authenticate(request);
        log.info("Method: authenticate() - User authenticated successfully with email: {}", request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(MessageConstants.LOGIN_SUCCESS, response));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        log.info("Method: refreshToken() - Token refresh request");
        service.refreshToken(request, response);
        log.info("Method: refreshToken() - Token refreshed successfully");
    }

} 