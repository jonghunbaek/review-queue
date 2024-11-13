package com.example.reviewqueue.review.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

public interface SseEmitterRepository {

    Optional<SseEmitter> findByUserId(Long userId);
    List<SseEmitter> findAll();
    SseEmitter save(Long userId, SseEmitter sseEmitter);
    void deleteByUserId(Long userId);
}