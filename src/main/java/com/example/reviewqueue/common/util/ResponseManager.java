package com.example.reviewqueue.common.util;

import com.example.reviewqueue.common.jwt.Tokens;
import com.example.reviewqueue.common.response.ResponseForm;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;

import java.io.IOException;

@Slf4j
public class ResponseManager {

    public static final String[] TOKEN_TYPE = {"access_token", "refresh_token"};
    public static final int COOKIE_MAX_AGE = 60 * 60 * 24;

    public static HttpHeaders setUpTokensToCookie(Tokens tokens) {
        ResponseCookie accessTokenCookie = createCookie(TOKEN_TYPE[0], tokens.getAccessToken(), false, COOKIE_MAX_AGE);
        ResponseCookie refreshTokenCookie = createCookie(TOKEN_TYPE[1], tokens.getRefreshToken(), true, COOKIE_MAX_AGE);

        return createCookieHeaders(accessTokenCookie, refreshTokenCookie);
    }

    private static ResponseCookie createCookie(String tokenType, String token, boolean isHttpOnly, long maxAge) {
        return ResponseCookie.from(tokenType, token)
                .httpOnly(isHttpOnly)
                .secure(false)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();
    }

    private static HttpHeaders createCookieHeaders(ResponseCookie accessCookie, ResponseCookie refreshCookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return headers;
    }

    public static HttpHeaders clearTokensFromCookie() {
        ResponseCookie accessCookie = createCookie(TOKEN_TYPE[0], "", false, 0);
        ResponseCookie refreshCookie = createCookie(TOKEN_TYPE[1], "", false, 0);

        return createCookieHeaders(accessCookie, refreshCookie);
    }

    public static void setUpResponse(HttpServletResponse response, ResponseForm responseForm, HttpStatus httpStatus) {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(responseForm));
            response.setStatus(httpStatus.value());
        } catch (IOException e) {
            log.error("error :: ", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
