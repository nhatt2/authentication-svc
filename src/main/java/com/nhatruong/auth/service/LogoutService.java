package com.nhatruong.auth.service;

import com.nhatruong.auth.constant.auth.JwtConstants;
import com.nhatruong.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(JwtConstants.HEADER_STRING);
        final String jwt;
        if (authHeader == null || !authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            return;
        }
        jwt = authHeader.substring(JwtConstants.TOKEN_PREFIX.length());
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
            log.info("Method: logout() - User token revoked and context cleared");
        }
    }
} 