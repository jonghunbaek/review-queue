package com.example.reviewqueue.auth.controller;

import com.example.reviewqueue.auth.service.dto.LoginRequest;
import com.example.reviewqueue.auth.service.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "인증", description = "회원가입, 로그인, 로그아웃")
public interface AuthApi {

    @Operation(summary = "회원가입")
    void signup(SignupRequest request, @Parameter(hidden = true) HttpServletResponse response);

    @Operation(summary = "로그인")
    void login(LoginRequest request, @Parameter(hidden = true) HttpServletResponse response);

    @Operation(summary = "로그아웃")
    void logout(@Parameter(hidden = true) HttpServletResponse response);
}
