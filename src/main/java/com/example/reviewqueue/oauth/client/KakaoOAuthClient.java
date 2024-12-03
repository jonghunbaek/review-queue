package com.example.reviewqueue.oauth.client;

import com.example.reviewqueue.oauth.client.dto.KakaoAccountInfoResponse;
import com.example.reviewqueue.oauth.client.dto.KakaoAuthTokenRequest;
import com.example.reviewqueue.oauth.client.dto.KakaoAuthTokenResponse;
import com.example.reviewqueue.oauth.service.dto.KakaoUserInfo;
import com.example.reviewqueue.oauth.service.dto.OAuthTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoOAuthClient {

    @Value("${oauth2.kakao.uri.authorization}")
    private String authorizationUri;

    @Value("${oauth2.kakao.uri.token}")
    private String tokenUri;

    @Value("${oauth2.kakao.uri.user-info}")
    private String userInfoUri;

    @Value("${oauth2.kakao.uri.redirect}")
    private String redirectUri;

    @Value("${oauth2.kakao.api-key.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.api-key.client-secret}")
    private String clientSecret;

    public void getAuthCode() {
        RestClient restClient = RestClient.builder()
                .baseUrl(authorizationUri)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .build();

        String response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("response_type", "code")
                        .build())
                .retrieve()
                .body(String.class);

    }

    public OAuthTokens getAuthToken(String code) {
        RestClient restClient = RestClient.builder()
                .baseUrl(tokenUri)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .build();

        KakaoAuthTokenRequest kakaoAuthTokenRequest = new KakaoAuthTokenRequest(clientId, clientSecret, redirectUri, code);

        return restClient.post()
                .body(kakaoAuthTokenRequest)
                .retrieve()
                .body(KakaoAuthTokenResponse.class)
                .toTokens();
    }

    public KakaoUserInfo getKakaoUserInfo(OAuthTokens authToken) {
        RestClient restClient = RestClient.builder()
                .baseUrl(userInfoUri)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.getAccessToken())
                .build();

        return restClient.get()
                .retrieve()
                .body(KakaoAccountInfoResponse.class)
                .toKakaoUserInfo();
    }
}
