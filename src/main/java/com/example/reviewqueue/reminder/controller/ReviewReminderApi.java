package com.example.reviewqueue.reminder.controller;

import com.example.reviewqueue.reminder.service.dto.ReminderInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "복습 알림", description = "복습 알림 조회 및 확인")
public interface ReviewReminderApi {

    @Operation(summary = "미확인 복습 알림 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    List<ReminderInfo> getUnreadReminders(@Parameter(hidden = true) Long memberId);

    @Operation(summary = "알림 확인 처리", security = @SecurityRequirement(name = "Bearer Authentication"))
    void readReviewReminder(@Parameter(description = "알림 ID") Long reminderId, @Parameter(hidden = true) Long memberId);
}
