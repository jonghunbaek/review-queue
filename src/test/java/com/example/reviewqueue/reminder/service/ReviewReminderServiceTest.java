package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.repository.StudyKeywordRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.reminder.repository.ReviewReminderRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(scripts = {"/member.sql", "/study.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewReminderServiceTest {

    @Autowired
    private ReviewReminderService reviewReminderService;

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
        Study study = studyRepository.findAll().get(0);
        LocalDate studyDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate.atTime(0,0), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스", dailyStudy);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스", dailyStudy);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2));

        LocalDate reviewDate = LocalDate.now();
        Review review = new Review(reviewDate.minusDays(1L), reviewDate, dailyStudy);
        reviewRepository.save(review);

        // when
        reviewReminderService.addReminder();
        List<ReviewReminder> reminders = reviewReminderRepository.findAll();

        // then
        assertThat(reminders).hasSize(1)
            .extracting("reminderDate")
            .containsExactlyInAnyOrder(reviewDate);
    }

    @DisplayName("사용자 아이디에 해당하는 읽지 않은 알람을 모두 조회한다.")
    @Test
    void findUnreadReminderReviewData() {
        Member member = memberRepository.findAll().get(0);
        Study study = studyRepository.findAll().get(0);
        LocalDate studyDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate.atTime(0,0), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스", dailyStudy);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스", dailyStudy);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2));

        LocalDate reviewDate1 = LocalDate.of(2024, 11, 11);
        LocalDate reviewDate2 = LocalDate.of(2024, 11, 15);
        Review review1 = new Review(reviewDate1.minusDays(1L), reviewDate1, dailyStudy);
        Review review2 = new Review(reviewDate2.minusDays(1L), reviewDate2, dailyStudy);
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewReminder reviewReminder1 = new ReviewReminder(reviewDate1, member);
        ReviewReminder reviewReminder2 = new ReviewReminder(reviewDate2, member);
        reviewReminderRepository.saveAll(List.of(reviewReminder1, reviewReminder2));

        // when
        List<ReviewsData> reviewsData = reviewReminderService.findUnreadReminderReviewData(member.getId());

        // then
        assertThat(reviewsData).hasSize(2)
            .extracting("reviewDate")
            .containsExactlyInAnyOrder(reviewDate1, reviewDate2);
    }
}