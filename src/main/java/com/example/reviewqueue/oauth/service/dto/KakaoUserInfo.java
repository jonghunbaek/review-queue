package com.example.reviewqueue.oauth.service.dto;

import lombok.Getter;

@Getter
public class KakaoUserInfo {

    private Long socialId;
    private String email;

    public KakaoUserInfo(Long socialId, String email) {
        this.socialId = socialId;
        this.email = email;
    }
}
