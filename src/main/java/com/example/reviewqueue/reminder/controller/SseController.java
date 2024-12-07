package com.example.reviewqueue.reminder.controller;

import com.example.reviewqueue.reminder.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequestMapping("/api/v1/sse")
@RequiredArgsConstructor
@RestController
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(Long memberId) {
        return sseService.subscribe(memberId);
    }
}
