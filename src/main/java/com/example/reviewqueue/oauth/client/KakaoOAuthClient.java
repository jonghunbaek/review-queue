package com.example.reviewqueue.oauth.client;

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

    public void getAuthToken() {

    }
}
