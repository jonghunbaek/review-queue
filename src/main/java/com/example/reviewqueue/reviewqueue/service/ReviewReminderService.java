package com.example.reviewqueue.reviewqueue.service;

import com.example.reviewqueue.reviewqueue.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class ReviewReminderService {

    public static final long TIME_OUT_MILLIS = 30 * 60 * 1000L;

    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = sseEmitterRepository.save(userId, new SseEmitter(TIME_OUT_MILLIS));

        emitter.onCompletion(() -> sseEmitterRepository.deleteByUserId(userId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteByUserId(userId));

        return emitter;
    }
}
