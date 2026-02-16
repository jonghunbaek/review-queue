package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.reminder.repository.ReviewReminderRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewReminderSchedulerTest {

    @Autowired
    private ReviewReminderScheduler reviewReminderScheduler;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyKeywordRepository studyKeywordRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewReminderRepository reviewReminderRepository;

    @DisplayName("매일 아침 5시 복습할 내용이 존재하면 복습 알람을 저장한다.")
    @Test
    void addReminder() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate studyDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate.atTime(0,0), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스", dailyStudy);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스", dailyStudy);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2));

        LocalDate reviewDate = LocalDate.now();
        Review review = new Review(reviewDate.minusDays(1L), reviewDate, dailyStudy);
        reviewRepository.save(review);

        // when
        reviewReminderScheduler.addReminder();
        List<ReviewReminder> reminders = reviewReminderRepository.findAll();

        // then
        assertThat(reminders).hasSize(1)
            .extracting("reminderDate")
            .containsExactlyInAnyOrder(reviewDate);
    }

    @DisplayName("오늘 복습이 여러 개이면 Review마다 Reminder가 각각 생성된다.")
    @Test
    void addReminder_multipleReviews() {
        // given
        Member member = memberRepository.save(new Member("test2@email.com", "password", "테스터2"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", LocalDate.of(2024, 10, 31).atTime(0, 0), study));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("9장 옵티마이저, p300-310", LocalDate.of(2024, 11, 1).atTime(0, 0), study));

        LocalDate today = LocalDate.now();
        Review review1 = reviewRepository.save(new Review(today.minusDays(1L), today, dailyStudy1));
        Review review2 = reviewRepository.save(new Review(today.minusDays(2L), today, dailyStudy2));

        // when
        reviewReminderScheduler.addReminder();
        List<ReviewReminder> reminders = reviewReminderRepository.findAll();

        // then
        assertThat(reminders).hasSize(2);
        assertThat(reminders).extracting(r -> r.getReview().getId())
                .containsExactlyInAnyOrder(review1.getId(), review2.getId());
    }

    @DisplayName("완료된 복습에 대해서는 Reminder가 생성되지 않는다.")
    @Test
    void addReminder_excludesCompletedReviews() {
        // given
        Member member = memberRepository.save(new Member("test3@email.com", "password", "테스터3"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스", LocalDate.of(2024, 10, 31).atTime(0, 0), study));

        LocalDate today = LocalDate.now();
        Review completedReview = new Review(today.minusDays(1L), today, dailyStudy);
        completedReview.completeReview();
        reviewRepository.save(completedReview);

        Review uncompletedReview = reviewRepository.save(new Review(today.minusDays(2L), today, dailyStudy));

        // when
        reviewReminderScheduler.addReminder();
        List<ReviewReminder> reminders = reviewReminderRepository.findAll();

        // then
        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getReview().getId()).isEqualTo(uncompletedReview.getId());
    }

    @DisplayName("서로 다른 사용자의 복습에 대해 각각 Reminder가 생성된다.")
    @Test
    void addReminder_multipleMembers() {
        // given
        Member member1 = memberRepository.save(new Member("user1@email.com", "password", "사용자1"));
        Member member2 = memberRepository.save(new Member("user2@email.com", "password", "사용자2"));
        Study study1 = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member1));
        Study study2 = studyRepository.save(new Study(StudyType.LECTURE, "Spring 강의", "좋은 강의", member2));
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("8장 인덱스", LocalDate.of(2024, 10, 31).atTime(0, 0), study1));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("1장 IoC", LocalDate.of(2024, 10, 31).atTime(0, 0), study2));

        LocalDate today = LocalDate.now();
        reviewRepository.save(new Review(today.minusDays(1L), today, dailyStudy1));
        reviewRepository.save(new Review(today.minusDays(1L), today, dailyStudy2));

        // when
        reviewReminderScheduler.addReminder();
        List<ReviewReminder> reminders = reviewReminderRepository.findAll();

        // then
        assertThat(reminders).hasSize(2);
        assertThat(reminders).extracting(r -> r.getMember().getId())
                .containsExactlyInAnyOrder(member1.getId(), member2.getId());
    }

    @DisplayName("오늘 복습할 내용이 없으면 Reminder가 생성되지 않는다.")
    @Test
    void addReminder_noReviewsToday() {
        // given
        Member member = memberRepository.save(new Member("test4@email.com", "password", "테스터4"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스", LocalDate.of(2024, 10, 31).atTime(0, 0), study));

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        reviewRepository.save(new Review(tomorrow.minusDays(1L), tomorrow, dailyStudy));

        // when
        reviewReminderScheduler.addReminder();
        List<ReviewReminder> reminders = reviewReminderRepository.findAll();

        // then
        assertThat(reminders).isEmpty();
    }
}
