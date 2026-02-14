package com.example.reviewqueue.reminder.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE", description = "실시간 알림 구독")
public interface SseApi {

    @Operation(summary = "SSE 구독")
    SseEmitter subscribe(@Parameter(description = "회원 ID") Long memberId);
}
