package com.example.reviewqueue.reminder.service.dto;

import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.review.service.dto.ReviewGeneralInfo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ReminderInfo {

    private final Long reminderId;
    private final LocalDate reminderDate;
    private final List<ReviewGeneralInfo> reviewsGeneralInfo;

    private ReminderInfo(Long reminderId, LocalDate reminderDate, List<ReviewGeneralInfo> reviewsGeneralInfo) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.reviewsGeneralInfo = reviewsGeneralInfo;
    }

    public static ReminderInfo from(ReviewReminder reminder, List<ReviewGeneralInfo> reviews) {
        return new ReminderInfo(reminder.getId(), reminder.getReminderDate(), reviews);
    }
}
