package com.example.reviewqueue.oauth.client.dto;

import com.example.reviewqueue.oauth.service.dto.KakaoUserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoAccountInfoResponse {

    private Long id;
    private KakaoAccount kakaoAccount;

    public KakaoUserInfo toKakaoUserInfo() {
        return new KakaoUserInfo(id, kakaoAccount.getEmail());
    }
}
