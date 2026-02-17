package com.example.reviewqueue.auth.controller;

import com.example.reviewqueue.auth.service.dto.LoginRequest;
import com.example.reviewqueue.auth.service.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증", description = "회원가입, 로그인, 로그아웃")
public interface AuthApi {

    @Operation(summary = "회원가입")
    ResponseEntity<Void> signup(@Valid SignupRequest request);

    @Operation(summary = "로그인")
    ResponseEntity<Void> login(@Valid LoginRequest request);

    @Operation(summary = "로그아웃")
    ResponseEntity<Void> logout();
}
