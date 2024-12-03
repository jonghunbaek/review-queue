package com.example.reviewqueue.oauth.client.dto;

import com.example.reviewqueue.oauth.service.dto.OAuthTokens;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoAuthTokenResponse {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    public OAuthTokens toTokens() {
        return new OAuthTokens(accessToken, refreshToken);
    }
}
