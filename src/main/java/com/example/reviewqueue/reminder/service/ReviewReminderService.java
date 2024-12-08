package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.reminder.exception.ReviewReminderException;
import com.example.reviewqueue.reminder.repository.ReviewReminderRepository;
import com.example.reviewqueue.reminder.service.dto.ReminderInfo;
import com.example.reviewqueue.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.E14003;
import static com.example.reviewqueue.common.util.GlobalValidator.validateAccessPermission;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReviewReminderService {

    private final ReviewService reviewService;

    private final ReviewReminderRepository reminderRepository;

    public List<ReminderInfo> findUnreadReminderReviewData(Long memberId) {
        List<ReviewReminder> reminders = reminderRepository.findAllByMemberIdAndIsReadIsFalse(memberId);

        validateAccessPermission(memberId, reminders.get(0).getMember().getId());

        return reminders.stream()
                .map(reminder -> ReminderInfo.from(reminder, reviewService.findAllReviewGeneralInfo(reminder.getReminderDate(), reminder.getMember().getId())))
                .toList();
    }

    public void read(Long reminderId, Long memberId) {
        ReviewReminder reviewReminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ReviewReminderException("reminderId :: " + reminderId, E14003));

        validateAccessPermission(memberId, reviewReminder.getMember().getId());

        reviewReminder.read();
    }
}
