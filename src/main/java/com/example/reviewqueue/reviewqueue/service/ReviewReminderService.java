package com.example.reviewqueue.reviewqueue.service;

import com.example.reviewqueue.reviewqueue.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewReminderService {

    public static final long TIME_OUT_MILLIS = 30 * 60 * 1000L;

    private final ReviewQueueService reviewQueueService;
    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = sseEmitterRepository.save(userId, new SseEmitter(TIME_OUT_MILLIS));

        emitter.onCompletion(() -> sseEmitterRepository.deleteByUserId(userId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteByUserId(userId));

        return emitter;
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void sendReviewsReminder() {
        List<SseEmitter> emitters = sseEmitterRepository.findAll();
        // TODO ::
        //  1. 알림을 보낼 복습 데이터 가져오기
        //  2. 알림보낸 내역 저장(SSE는 실시간 알림을 위함이므로 로그인하지 않은 사용자에 대해서도 알림을 보내야함)
    }
}
