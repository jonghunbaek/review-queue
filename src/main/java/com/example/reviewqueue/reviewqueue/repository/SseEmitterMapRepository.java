package com.example.reviewqueue.reviewqueue.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 테스트용 emitter 저장소.
 */
@Repository
public class SseEmitterMapRepository implements SseEmitterRepository {

    private Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public Optional<SseEmitter> findByUserId(Long userId) {
        return Optional.ofNullable(emitters.get(userId));
    }

    @Override
    public List<SseEmitter> findAll() {
        return emitters.values().stream().toList();
    }

    @Override
    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        return emitters.put(userId, sseEmitter);
    }

    @Override
    public void deleteByUserId(Long userId) {
        emitters.remove(userId);
    }
}
