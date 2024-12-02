package com.example.reviewqueue.oauth.controller;

import com.example.reviewqueue.oauth.controller.dto.KakaoAuthCode;
import com.example.reviewqueue.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/kakao/login")
    public void kakaoLogin() {
        oAuthService.getAuthCode();
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallback(KakaoAuthCode kakaoAuthCode) {
        oAuthService.getAuthToken(kakaoAuthCode.getCode());
    }
}
