package com.example.reviewqueue.common.jwt;

import com.example.reviewqueue.common.response.ResponseForm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import static com.example.reviewqueue.common.response.ResponseCode.*;
import static com.example.reviewqueue.common.util.ResponseManager.setUpResponse;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        ResponseForm exceptionResponse = createExceptionMessage((Exception) request.getAttribute(JwtAuthenticationFilter.EXCEPTION_KEY));
        setUpResponse(response, exceptionResponse, UNAUTHORIZED);
    }

    private ResponseForm createExceptionMessage(Exception e) {
        if (e instanceof SignatureException) {
            return ResponseForm.of(E00004);
        }

        if (e instanceof MalformedJwtException) {
            return ResponseForm.of(E00006);
        }

        if (e instanceof ExpiredJwtException) {
            return ResponseForm.of(E00003);
        }

        if (e instanceof IllegalStateException) {
            return ResponseForm.of(E00005);
        }

        return ResponseForm.of(E00001);
    }
}
