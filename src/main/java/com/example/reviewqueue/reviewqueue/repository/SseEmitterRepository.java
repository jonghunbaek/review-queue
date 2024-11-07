package com.example.reviewqueue.reviewqueue.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

public interface SseEmitterRepository {

    Optional<SseEmitter> findByUserId(Long userId);
    SseEmitter save(Long userId, SseEmitter sseEmitter);
    void deleteByUserId(Long userId);
}
