package com.example.reviewqueue.oauth.controller;

import com.example.reviewqueue.member.service.MemberService;
import com.example.reviewqueue.oauth.controller.dto.KakaoAuthCode;
import com.example.reviewqueue.oauth.service.OAuthService;
import com.example.reviewqueue.oauth.service.dto.KakaoUserInfo;
import com.example.reviewqueue.oauth.service.dto.OAuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private final OAuthService oAuthService;
    private final MemberService memberService;

    // TODO :: 프론트 페이지 완성되면 인가 코드 받는 로직은 프론트로 이동 예정
    @GetMapping("/kakao/login")
    public void kakaoLogin() {
        oAuthService.getAuthCode();
    }

    // TODO :: 프론트 페이지 완성되면 인가 코드 받는 로직은 프론트로 이동 예정
    @GetMapping("/kakao/callback")
    public void kakaoCallback(KakaoAuthCode kakaoAuthCode) {
        OAuthTokens authToken = oAuthService.getAuthToken(kakaoAuthCode.getCode()); // 1. 토큰 발급
        KakaoUserInfo kakaoUserInfo = oAuthService.getKakaoUserInfo(authToken); // 2. 카카오 사용자 정보 조회
        memberService.registerIfAbsent(kakaoUserInfo.getSocialId(), kakaoUserInfo.getEmail()); // 3. DB 계정 정보 없다면 새로 가입, 있다면 바로 로그인
        // 4.  토큰 발급
    }
}
