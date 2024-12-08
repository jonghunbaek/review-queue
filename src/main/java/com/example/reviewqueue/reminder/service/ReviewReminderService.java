package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.reminder.repository.ReviewReminderRepository;
import com.example.reviewqueue.review.service.ReviewService;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.reviewqueue.common.util.GlobalValidator.validateAccessPermission;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReviewReminderService {

    private final ReviewService reviewService;

    private final ReviewReminderRepository reminderRepository;

    public List<ReviewsData> findUnreadReminderReviewData(Long memberId) {
        List<ReviewReminder> reminders = reminderRepository.findAllByMemberIdAndIsReadIsFalse(memberId);

        validateAccessPermission(memberId, reminders.get(0).getMember().getId());

        return reminders.stream()
                .map(reminder -> reviewService.findAllBy(reminder.getReminderDate(), reminder.getMember().getId()))
                .toList();
    }
}
