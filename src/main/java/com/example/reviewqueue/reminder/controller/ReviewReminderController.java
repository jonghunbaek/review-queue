package com.example.reviewqueue.reminder.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.reminder.service.ReviewReminderService;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/review-reminders")
@RequiredArgsConstructor
@RestController
public class ReviewReminderController {

    private final ReviewReminderService reviewReminderService;

    /**
     *  확인하지 않은 복습 알림을 모두 가져오기
     */
    @GetMapping
    public List<ReviewsData> getUnreadReminders(@AuthenticatedMember Long memberId) {
        return reviewReminderService.findUnreadReminderReviewData(memberId);
    }
}
