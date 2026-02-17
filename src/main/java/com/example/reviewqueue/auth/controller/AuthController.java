package com.example.reviewqueue.auth.controller;

import com.example.reviewqueue.auth.service.AuthService;
import com.example.reviewqueue.auth.service.dto.LoginRequest;
import com.example.reviewqueue.auth.service.dto.SignupRequest;
import com.example.reviewqueue.common.jwt.Tokens;
import com.example.reviewqueue.token.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.reviewqueue.common.util.ResponseManager.clearTokensFromCookie;
import static com.example.reviewqueue.common.util.ResponseManager.setUpTokensToCookie;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        Long memberId = authService.signUp(request);
        Tokens tokens = tokenService.createTokens(memberId);
        HttpHeaders headers = setUpTokensToCookie(tokens);

        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        Long memberId = authService.signIn(request);
        Tokens tokens = tokenService.createTokens(memberId);
        HttpHeaders headers = setUpTokensToCookie(tokens);

        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        HttpHeaders headers = clearTokensFromCookie();

        return ResponseEntity.ok().headers(headers).build();
    }
}
