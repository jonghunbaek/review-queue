package com.example.reviewqueue.common.config.jwt;

import com.example.reviewqueue.common.config.security.SecurityProperties;
import com.example.reviewqueue.common.jwt.JwtAccessDeniedHandler;
import com.example.reviewqueue.common.jwt.JwtAuthenticationEntryPoint;
import com.example.reviewqueue.common.jwt.JwtAuthenticationFilter;
import com.example.reviewqueue.common.jwt.JwtManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Value("${jwt.issuer}")
    private String issuer;

    @Bean
    public JwtManager jwtManager() {
        return new JwtManager(secretKey, accessExpiration, refreshExpiration, issuer);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtManager jwtManager, SecurityProperties securityProperties) {
        return new JwtAuthenticationFilter(jwtManager, securityProperties.whitelist());
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }
}
