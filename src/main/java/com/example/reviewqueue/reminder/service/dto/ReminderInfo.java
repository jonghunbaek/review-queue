package com.example.reviewqueue.reminder.service.dto;

import com.example.reviewqueue.reminder.domain.ReviewReminder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReminderInfo {

    private final Long reminderId;
    private final LocalDate reminderDate;
    private final Long reviewId;
    private final String studyTitle;
    private final String dailyStudyRange;

    private ReminderInfo(Long reminderId, LocalDate reminderDate, Long reviewId, String studyTitle, String dailyStudyRange) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.reviewId = reviewId;
        this.studyTitle = studyTitle;
        this.dailyStudyRange = dailyStudyRange;
    }

    public static ReminderInfo from(ReviewReminder reminder) {
        return new ReminderInfo(
                reminder.getId(),
                reminder.getReminderDate(),
                reminder.getReview().getId(),
                reminder.getReview().getDailyStudy().getStudy().getTitle(),
                reminder.getReview().getDailyStudy().getStudyRange()
        );
    }
}
