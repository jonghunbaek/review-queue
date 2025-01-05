package com.example.reviewqueue.oauth.service;

import com.example.reviewqueue.oauth.client.KakaoOAuthClient;
import com.example.reviewqueue.oauth.service.dto.KakaoUserInfo;
import com.example.reviewqueue.oauth.service.dto.OAuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {

    private final KakaoOAuthClient kakaoOAuthClient;

    public String getLoginUrl() {
        return kakaoOAuthClient.getLoginUrl();
    }

    public OAuthTokens getAuthToken(String code) {
        return kakaoOAuthClient.getAuthToken(code);
    }

    public KakaoUserInfo getKakaoUserInfo(OAuthTokens authToken) {
        return kakaoOAuthClient.getKakaoUserInfo(authToken);
    }
}
