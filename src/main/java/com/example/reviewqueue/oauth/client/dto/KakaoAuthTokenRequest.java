package com.example.reviewqueue.oauth.client.dto;

import lombok.Getter;

@Getter
public class KakaoAuthTokenRequest {

    private String grant_type = "authorization_code";
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String code;

    public KakaoAuthTokenRequest(String client_id, String client_secret, String redirect_uri, String code) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.code = code;
    }
}
