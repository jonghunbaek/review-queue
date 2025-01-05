package com.example.reviewqueue.oauth.client;

import com.example.reviewqueue.oauth.client.dto.KakaoAccountInfoResponse;
import com.example.reviewqueue.oauth.client.dto.KakaoAuthTokenResponse;
import com.example.reviewqueue.oauth.service.dto.KakaoUserInfo;
import com.example.reviewqueue.oauth.service.dto.OAuthTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    public String getLoginUrl() {
        return authorizationUri + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code" + "&prompt=login";
    }

    public OAuthTokens getAuthToken(String code) {
        RestClient restClient = RestClient.builder()
                .baseUrl(tokenUri)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .build();

        MultiValueMap<String, String> data = createRequestData(code);

        return restClient.post()
                .body(data)
                .retrieve()
                .body(KakaoAuthTokenResponse.class)
                .toTokens();
    }

    /**
     *  application/x-www-form-urlencoded 형식은 MultiValueMap을 사용해야 Spring의 컨버터가 작동
     */
    private MultiValueMap<String, String> createRequestData(String code) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.add("grant_type", "authorization_code");
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("code", code);
        data.add("redirect_uri", redirectUri);

        return data;
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
