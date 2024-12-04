package com.example.reviewqueue.token.service;

import com.example.reviewqueue.common.jwt.JwtManager;
import com.example.reviewqueue.common.jwt.Tokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {

    private final JwtManager jwtManager;

    public Tokens createTokens(Long memberId) {
        Instant now = Instant.now();

        return jwtManager.createTokens(memberId, now);
    }
}
