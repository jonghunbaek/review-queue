package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.common.response.ResponseCode;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.reminder.repository.ReviewReminderRepository;
import com.example.reviewqueue.review.service.ReviewService;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewReminderService {

    private final ReviewService reviewService;
    private final SseService sseService;

    private final MemberRepository memberRepository;
    private final ReviewReminderRepository reminderRepository;

    // TODO :: 나중에 JdbcTemplate을 활용해 벌크 INSERT로 변경하기
    @Scheduled(cron = "0 0 5 * * ?")
    public void addReminder() {
        saveReviewReminder()
            .forEach(memberId -> sseService.sendReminder(memberId, ResponseCode.E14001));
    }

    private List<Long> saveReviewReminder() {
        LocalDate today = LocalDate.now();
        List<Long> memberIds = reviewService.findMemberIdsByReviewDate(today);
        List<Member> members = memberRepository.findAllById(memberIds);

        List<ReviewReminder> reminders = members.stream()
                .map(member -> new ReviewReminder(today, member))
                .toList();

        reminderRepository.saveAll(reminders);

        return memberIds;
    }

    public List<ReviewsData> findUnreadReminderReviewData(Long memberId) {
        List<ReviewReminder> reminders = reminderRepository.findAllByMemberIdAndIsReadIsFalse(memberId);

        return reminders.stream()
            .map(reminder -> reviewService.findAllReviewDataByDateAndMemberId(reminder.getReminderDate(), reminder.getMember().getId()))
            .toList();
    }
}
