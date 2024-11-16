package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.reminder.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SseService {

    public static final long TIME_OUT_MILLIS = 30 * 60 * 1000L;

    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = sseEmitterRepository.save(userId, new SseEmitter(TIME_OUT_MILLIS));

        emitter.onCompletion(() -> sseEmitterRepository.deleteByUserId(userId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteByUserId(userId));

        return emitter;
    }

    public void sendReminder(List<Long> memberIds) {
        Map<Long, SseEmitter> emittersByMemberId = sseEmitterRepository.findAllByMemberIds(memberIds);

    }

    private String createEventId(Long userId) {
        return userId.toString() + System.currentTimeMillis();
    }
}
