package com.example.reviewqueue.reminder.repository;

import com.example.reviewqueue.common.config.querydsl.QuerydslConfig;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class ReviewReminderRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewReminderRepository reminderRepository;

    @DisplayName("읽지 않은 알림을 조회한다.")
    @Test
    void existsReviewRemindersByMemberIdAndReadIsFalse() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스", LocalDate.of(2024, 10, 31).atTime(0, 0), study));
        Review review1 = reviewRepository.save(new Review(LocalDate.of(2024, 11, 19), LocalDate.of(2024, 11, 20), dailyStudy));
        Review review2 = reviewRepository.save(new Review(LocalDate.of(2024, 11, 20), LocalDate.of(2024, 11, 21), dailyStudy));

        ReviewReminder reminder1 = new ReviewReminder(LocalDate.of(2024, 11, 20), member, review1);
        ReviewReminder reminder2 = new ReviewReminder(LocalDate.of(2024, 11, 21), member, review2);
        reminder1.read();

        reminderRepository.saveAll(List.of(reminder1, reminder2));

        // when
        List<ReviewReminder> reminders = reminderRepository.findAllByMemberIdAndIsReadIsFalse(member.getId());

        // then
        assertThat(reminders).hasSize(1);
    }
}
