package com.example.reviewqueue.reminder.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

public interface SseEmitterRepository {

    Optional<SseEmitter> findByMemberId(Long memberId);
    List<SseEmitter> findAll();
    SseEmitter save(Long userId, SseEmitter sseEmitter);
    void deleteByUserId(Long userId);
}
