package com.nhatruong.auth.filter;

import com.nhatruong.auth.constant.api.ApiConstants;
import com.nhatruong.auth.constant.auth.JwtConstants;
import com.nhatruong.auth.repository.TokenRepository;
import com.nhatruong.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader(JwtConstants.HEADER_STRING);
            final String jwt;
            final String userEmail;

            String requestPath = request.getServletPath();
            if (isWhiteListedPath(requestPath)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (authHeader == null || !authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(JwtConstants.TOKEN_PREFIX.length());
            userEmail = jwtService.extractUserName(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                var isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);

                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Method: doFilterInternal() - User authenticated: {}", userEmail);
                } else {
                    log.warn("Method: doFilterInternal() - Invalid token for user: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.error("Method: doFilterInternal() - Error during JWT authentication", e);
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean isWhiteListedPath(String path) {
        for (String whiteListUrl : ApiConstants.WHITE_LIST_URLS) {
            if (path.startsWith(whiteListUrl.replace("/**", ""))) {
                return true;
            }
        }
        return false;
    }
}
