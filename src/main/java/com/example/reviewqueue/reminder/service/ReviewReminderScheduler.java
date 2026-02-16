package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.common.response.ResponseCode;
import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.reminder.repository.ReviewReminderRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewReminderScheduler {

    private final SseService sseService;

    private final ReviewRepository reviewRepository;
    private final ReviewReminderRepository reminderRepository;

    // TODO :: 나중에 JdbcTemplate을 활용해 벌크 INSERT로 변경하기
    @Scheduled(cron = "0 0 5 * * ?")
    public void addReminder() {
        saveReviewReminder()
            .forEach(memberId -> sseService.sendReminder(memberId, ResponseCode.E14001));
    }

    private List<Long> saveReviewReminder() {
        LocalDate today = LocalDate.now();
        List<Review> reviews = reviewRepository.findAllByReviewDateAndIsCompletedIsFalse(today);

        List<ReviewReminder> reminders = reviews.stream()
                .map(review -> new ReviewReminder(today, review.getMember(), review))
                .toList();

        reminderRepository.saveAll(reminders);

        return reviews.stream()
                .map(review -> review.getMember().getId())
                .distinct()
                .toList();
    }
}
