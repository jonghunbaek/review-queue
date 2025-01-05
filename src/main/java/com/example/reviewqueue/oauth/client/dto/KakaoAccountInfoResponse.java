package com.example.reviewqueue.oauth.client.dto;

import com.example.reviewqueue.oauth.service.dto.KakaoUserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class KakaoAccountInfoResponse {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public KakaoUserInfo toKakaoUserInfo() {
        return new KakaoUserInfo(id, kakaoAccount.getEmail());
    }
}
