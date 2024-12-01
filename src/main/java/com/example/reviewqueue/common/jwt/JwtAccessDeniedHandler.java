package com.example.reviewqueue.common.jwt;

import com.example.reviewqueue.common.response.ResponseForm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static com.example.reviewqueue.common.response.ResponseCode.E00002;
import static com.example.reviewqueue.common.util.ResponseManager.setUpResponse;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        setUpResponse(response, ResponseForm.of(E00002), FORBIDDEN);
    }
}
