package com.example.reviewqueue.common.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtManager {

    private final long accessExpiration;
    private final long refreshExpiration;
    private final String issuer;
    private final SecretKey secretKey;

    public JwtManager(String secretKey, long accessExpiration, long refreshExpiration, String issuer) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
    }

    public Tokens createTokens(Long memberId, Instant now) {
        String accessToken = createAccessToken(memberId, now);
        String refreshToken = createRefreshToken(now);

        return new Tokens(accessToken, refreshToken);
    }

    public String createAccessToken(Long memberId, Instant now) {
        return createToken(memberId.toString(), accessExpiration, now);
    }

    public String createRefreshToken(Instant now) {
        return createToken("", refreshExpiration, now);
    }

    private String createToken(String subject, long expiration, Instant now) {
        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS512)
                .subject(subject)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration, ChronoUnit.SECONDS)))
                .compact();
    }

    public String parseAccessToken(String token) {
        JwtParser jwtParser = createJwtParser();

        return parseToken(token, jwtParser)
                .getSubject();
    }

    public void validateRefreshToken(String refreshToken) {
        JwtParser jwtParser = createJwtParser();

        parseToken(refreshToken, jwtParser);
    }

    private JwtParser createJwtParser() {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build();
    }

    private Claims parseToken(String token, JwtParser jwtParser) {
        return jwtParser.parseSignedClaims(token)
                .getPayload();
    }

    public String reissueAccessToken(String accessTokens, Instant now) {
        String memberId = decodeJwtPayload(accessTokens);

        return createToken(memberId, accessExpiration, now);
    }

    /**
     * 만료된 토큰을 jwtParser로 파싱하게 되면 만료 예외가 발생하므로 토큰의 subject부분만 디코딩
     * @param oldAccessToken 만료된 토큰
     * @return
     */
    private String decodeJwtPayload(String oldAccessToken) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8), Map.class)
                    .get("sub")
                    .toString();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
