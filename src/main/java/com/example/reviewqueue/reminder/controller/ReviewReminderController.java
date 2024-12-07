package com.example.reviewqueue.reminder.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.reminder.service.ReviewReminderService;
import com.example.reviewqueue.reminder.service.SseService;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequestMapping("/api/v1/review-reminders")
@RequiredArgsConstructor
@RestController
public class ReviewReminderController {

    private final ReviewReminderService reviewReminderService;

    @GetMapping
    public List<ReviewsData> getUnreadReminders(@AuthenticatedMember Long memberId) {
        return reviewReminderService.findUnreadReminderReviewData(memberId);
    }
}
