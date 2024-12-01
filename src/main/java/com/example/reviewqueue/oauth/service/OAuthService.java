package com.example.reviewqueue.oauth.service;

import com.example.reviewqueue.oauth.client.KakaoOAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
}
