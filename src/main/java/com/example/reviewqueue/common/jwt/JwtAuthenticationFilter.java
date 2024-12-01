package com.example.reviewqueue.common.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTH_TYPE = "Bearer ";
    public static final String EXCEPTION_KEY = "exception";

    private final JwtManager jwtManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .ifPresent(token -> authorize(request, token));

        filterChain.doFilter(request, response);
    }

    private void authorize(HttpServletRequest request, String tokenWithBearer) {
        try {
            String accessToken = extractToken(tokenWithBearer);

            Authentication authentication = createAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException | IllegalStateException e) {
            request.setAttribute(EXCEPTION_KEY, e);
            log.error("e :: ", e);
        }
    }

    private String extractToken(String tokenWithBearer) {
        validateNone(tokenWithBearer);
        String authType = tokenWithBearer.substring(0, AUTH_TYPE.length());

        validateAuthType(authType);
        return tokenWithBearer.substring(AUTH_TYPE.length());
    }

    private void validateNone(String tokenWithBearer) {
        if (!StringUtils.hasText(tokenWithBearer)) {
            throw new IllegalArgumentException("토큰 값이 존재하지 않습니다.");
        }
    }

    private void validateAuthType(String authType) {
        if (!authType.equalsIgnoreCase(AUTH_TYPE)) {
            throw new IllegalArgumentException("AUTH_TYPE이 일치하지 않습니다. AUTH_TYPE :: " + authType);
        }
    }

    private Authentication createAuthentication(String accessToken) {
        String memberId = jwtManager.parseAccessToken(accessToken);

        return new UsernamePasswordAuthenticationToken(memberId, "", Collections.singletonList(new SimpleGrantedAuthority("")));
    }
}
