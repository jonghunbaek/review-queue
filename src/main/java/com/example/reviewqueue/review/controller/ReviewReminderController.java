package com.example.reviewqueue.review.controller;

import com.example.reviewqueue.reminder.service.ReviewReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequestMapping("/reviews/reminder")
@RequiredArgsConstructor
@RestController
public class ReviewReminderController {

    private final ReviewReminderService reviewReminderService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(Long memberId) {
        return reviewReminderService.subscribe(memberId);
    }
}
