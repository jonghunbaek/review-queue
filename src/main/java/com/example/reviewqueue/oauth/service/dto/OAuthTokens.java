package com.example.reviewqueue.oauth.service.dto;

import lombok.Getter;

@Getter
public class OAuthTokens {

    private String accessToken;
    private String refreshToken;

    public OAuthTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
