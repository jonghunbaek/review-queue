package com.example.reviewqueue.common.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String EXCEPTION_KEY = "exception";
    public static final String[] WHITE_LIST = {
        "/h2-console/**",
        "/oauth/kakao/**",
        "/health/**",
        "/token/**"
    };

    private final JwtManager jwtManager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();

        return Arrays.stream(WHITE_LIST)
                .anyMatch(url -> matcher.match(url, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = getTokenFromCookies(request.getCookies());

        authorize(request, accessToken);
        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("access_token"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재 하지 않습니다."))
                .getValue();
    }

    private void authorize(HttpServletRequest request, String accessToken) {
        try {
            validateNone(accessToken);

            Authentication authentication = createAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException | IllegalStateException e) {
            request.setAttribute(EXCEPTION_KEY, e);
            log.error("e :: ", e);
        }
    }

    private void validateNone(String tokenWithBearer) {
        if (!StringUtils.hasText(tokenWithBearer)) {
            throw new IllegalArgumentException("토큰 값이 존재하지 않습니다.");
        }
    }

    private Authentication createAuthentication(String accessToken) {
        String memberId = jwtManager.parseAccessToken(accessToken);

        return new UsernamePasswordAuthenticationToken(memberId, "", Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
