package com.example.reviewqueue.reminder.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SseEmitterRepository {

    Optional<SseEmitter> findByUserId(Long memberId);
    List<SseEmitter> findAll();
    Map<Long, SseEmitter> findAllByMemberIds(List<Long> memberIds);
    SseEmitter save(Long userId, SseEmitter sseEmitter);
    void deleteByUserId(Long userId);
}
