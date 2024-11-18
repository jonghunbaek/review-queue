package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.common.response.ResponseCode;
import com.example.reviewqueue.reminder.exception.ReviewReminderException;
import com.example.reviewqueue.reminder.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static com.example.reviewqueue.common.response.ResponseCode.E14000;
import static com.example.reviewqueue.common.response.ResponseCode.E14002;

@RequiredArgsConstructor
@Service
public class SseService {

    public static final long TIME_OUT_MILLIS = 30 * 60 * 1000L;

    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = sseEmitterRepository.save(memberId, new SseEmitter(TIME_OUT_MILLIS));

        emitter.onCompletion(() -> sseEmitterRepository.deleteByUserId(memberId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteByUserId(memberId));

        sendReminder(memberId, E14002);

        return emitter;
    }

    public void sendReminder(Long memberId, ResponseCode eventMessage) {
        sseEmitterRepository.findByMemberId(memberId)
            .ifPresent(emitter -> sendEvent(memberId, emitter, eventMessage));
    }

    private void sendEvent(Long memberId, SseEmitter emitter, ResponseCode eventMessage) {
        try {
            emitter.send(createEvent(memberId, eventMessage));
        } catch (IOException e) {
            throw new ReviewReminderException(E14000);
        }
    }

    private SseEmitter.SseEventBuilder createEvent(Long memberId, ResponseCode eventMessage) {
        return SseEmitter.event()
            .id(createEventId(memberId))
            .data(eventMessage.getMessage());
    }

    private String createEventId(Long memberId) {
        return memberId.toString() + System.currentTimeMillis();
    }
}
