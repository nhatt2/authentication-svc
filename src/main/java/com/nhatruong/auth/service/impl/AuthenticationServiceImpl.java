package com.nhatruong.auth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhatruong.auth.constant.auth.JwtConstants;
import com.nhatruong.auth.constant.auth.TokenType;
import com.nhatruong.auth.constant.message.MessageConstants;
import com.nhatruong.auth.dto.User;
import com.nhatruong.auth.dto.UserDto;
import com.nhatruong.auth.exception.CustomAuthenticationException;
import com.nhatruong.auth.exception.TokenException;
import com.nhatruong.auth.exception.UserNotFoundException;
import com.nhatruong.auth.model.AuthenticationRequest;
import com.nhatruong.auth.model.AuthenticationResponse;
import com.nhatruong.auth.model.RegisterRequest;
import com.nhatruong.auth.repository.TokenRepository;
import com.nhatruong.auth.repository.UserRepository;
import com.nhatruong.auth.service.AuthenticationService;
import com.nhatruong.auth.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nhatruong.auth.dto.Token;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        try {
            log.info("Method: register() - Attempting to register user with email: {}", request.getEmail());
            
            if (repository.findByEmail(request.getEmail()).isPresent()) {
                throw new CustomAuthenticationException("User already exists with email: " + request.getEmail());
            }
            
            var user = User.builder()
                    .firstName(request.getFirstname())
                    .lastName(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .userRole(request.getRole())
                    .build();
            
            var savedUser = repository.save(user);
            log.info("Method: register() - User saved successfully with ID: {}", savedUser.getId());
            
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            
            log.info("Method: register() - User registration completed for email: {}", request.getEmail());
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .user(UserDto.fromEntity(savedUser))
                    .build();
        } catch (Exception e) {
            log.error("Method: register() - Error during user registration for email: {}", request.getEmail(), e);
            throw new CustomAuthenticationException("Registration failed: " + e.getMessage());
        }
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            log.info("Method: authenticate() - Attempting to authenticate user with email: {}", request.getEmail());
            
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException(
                            String.format(MessageConstants.USER_NOT_FOUND_MESSAGE, request.getEmail())));
            
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            
            log.info("Method: authenticate() - User authenticated successfully: {}", request.getEmail());
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .user(UserDto.fromEntity(user))
                    .build();
        } catch (BadCredentialsException e) {
            log.error("Method: authenticate() - Authentication failed for email: {}", request.getEmail());
            throw new CustomAuthenticationException(MessageConstants.INVALID_CREDENTIALS_MESSAGE);
        } catch (Exception e) {
            log.error("Method: authenticate() - Error during authentication for email: {}", request.getEmail(), e);
            throw new CustomAuthenticationException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String refreshToken;
            final String userEmail;
            
            if (authHeader == null || !authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
                throw new TokenException(MessageConstants.TOKEN_INVALID_MESSAGE);
            }
            
            refreshToken = authHeader.substring(JwtConstants.TOKEN_PREFIX.length());
            userEmail = jwtService.extractUserName(refreshToken);
            
            if (userEmail != null) {
                var user = this.repository.findByEmail(userEmail)
                        .orElseThrow(() -> new UserNotFoundException(
                                String.format(MessageConstants.USER_NOT_FOUND_MESSAGE, userEmail)));
                
                if (jwtService.isTokenValid(refreshToken, user)) {
                    var accessToken = jwtService.generateToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);
                    
                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                    log.info("Method: refreshToken() - Token refreshed successfully for user: {}", userEmail);
                } else {
                    throw new TokenException(MessageConstants.TOKEN_INVALID_MESSAGE);
                }
            } else {
                throw new TokenException(MessageConstants.TOKEN_INVALID_MESSAGE);
            }
        } catch (Exception e) {
            log.error("Method: refreshToken() - Error during token refresh", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
