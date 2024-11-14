package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.reminder.repository.SseEmitterRepository;
import com.example.reviewqueue.review.service.ReviewService;
import com.example.reviewqueue.review.service.dto.ReviewData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SseService {

    public static final long TIME_OUT_MILLIS = 30 * 60 * 1000L;

    private final ReviewService reviewService;
    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = sseEmitterRepository.save(userId, new SseEmitter(TIME_OUT_MILLIS));

        emitter.onCompletion(() -> sseEmitterRepository.deleteByUserId(userId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteByUserId(userId));

        return emitter;
    }

    // TODO :: 복습할 내용이 있는 경우에만 알림을 보내고, 실제 데이터는 알림을 클릭했을 때 조회하도록 변경하기
    @Scheduled(cron = "0 0 5 * * ?")
    public void sendReviewsReminder() {
        List<SseEmitter> emitters = sseEmitterRepository.findAll();
        List<ReviewData> reviewData = reviewService.findAllReviewDataByDate(LocalDate.now());

        // TODO ::
        //  2. 알림보낸 내역 저장(SSE는 실시간 알림을 위함이므로 로그인하지 않은 사용자에 대해서도 알림을 보내야함)
    }
}
