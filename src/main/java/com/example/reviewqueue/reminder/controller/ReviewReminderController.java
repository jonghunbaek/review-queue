package com.example.reviewqueue.reminder.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.reminder.service.ReviewReminderService;
import com.example.reviewqueue.reminder.service.dto.ReminderInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/review-reminders")
@RequiredArgsConstructor
@RestController
public class ReviewReminderController implements ReviewReminderApi {

    private final ReviewReminderService reviewReminderService;

    /**
     *  확인하지 않은 복습 알림을 모두 가져오기
     */
    @GetMapping
    public List<ReminderInfo> getUnreadReminders(@AuthenticatedMember Long memberId) {
        return reviewReminderService.findUnreadReminderReviewData(memberId);
    }

    /**
     *  알림 확인 처리
     */
    @PostMapping("/check/{reminderId}")
    public void readReviewReminder(@PathVariable Long reminderId, @AuthenticatedMember Long memberId) {
        reviewReminderService.read(reminderId, memberId);
    }
}
