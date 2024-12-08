package com.example.reviewqueue.review.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.repository.StudyKeywordRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(scripts = {"/member.sql", "/study.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

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

    @DisplayName("복습 조건에 따라 복습할 학습을 저장한다.")
    @Test
    void saveReviewQueue() {
        // given
        Study study = studyRepository.findAll().get(0);
        LocalDate startDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("학습 1", startDate.atTime(0,0), study));
        ReviewQueueSave reviewQueueSave = new ReviewQueueSave(dailyStudy.getId(), 5, 1, 2, 3, 4, 5);

        // when
        reviewService.save(reviewQueueSave, 1L);
        List<Review> reviews = reviewRepository.findAll();

        // then
        assertThat(reviews).hasSize(5)
                .extracting("reviewDate")
                .containsExactlyInAnyOrder(
                        LocalDate.of(2024, 11, 1),
                        LocalDate.of(2024, 11, 3),
                        LocalDate.of(2024, 11, 6),
                        LocalDate.of(2024, 11, 10),
                        LocalDate.of(2024, 11, 15)
                );
    }

    @DisplayName("파라미터로 전달 받은 날짜에 해당하는 복습 데이터를 가져온다.")
    @Test
    void findReviewDataByDate() {
        Member member = memberRepository.findAll().get(0);
        Study study = studyRepository.findAll().get(0);
        LocalDate studyDate1 = LocalDate.of(2024, 10, 31);
        LocalDate studyDate2 = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate1.atTime(0,0), study));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p211-220", studyDate2.atTime(0,0), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword3 = new StudyKeyword("클러스터드 인덱스", "PK 기반의 인덱스", dailyStudy2);
        StudyKeyword keyword4 = new StudyKeyword("세컨더리 인덱스", "PK 이외의 인덱스", dailyStudy2);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2, keyword3, keyword4));

        ReviewQueueSave reviewQueueSave1 = new ReviewQueueSave(dailyStudy1.getId(), 5, 1, 2, 3, 4, 5);
        ReviewQueueSave reviewQueueSave2 = new ReviewQueueSave(dailyStudy2.getId(), 5, 1, 2, 3, 4, 5);
        reviewService.save(reviewQueueSave1, 1L);
        reviewService.save(reviewQueueSave2, 1L);

        // when
        LocalDate reviewDate = LocalDate.of(2024, 11, 3);
        ReviewsData reviewsData = reviewService.findAllBy(reviewDate, member.getId());

        // then
        assertAll(
                () -> assertThat(reviewsData.getReviews()).hasSize(2),
                () -> assertThat(reviewsData.getReviews().get(0).getReviewKeywords()).hasSize(2)
                        .extracting("keyword")
                        .containsExactlyInAnyOrder(
                                "B-Tree 인덱스",
                                "R-Tree 인덱스"
                        ),
                () -> assertThat(reviewsData.getReviews().get(1).getReviewKeywords()).hasSize(2)
                        .extracting("keyword")
                        .containsExactlyInAnyOrder(
                                "클러스터드 인덱스",
                                "세컨더리 인덱스"
                        )
        );
    }

    @DisplayName("파라미터로 전달 받은 복습 날짜에 복습 데이터를 가지고 있는 사용자의 아이디 목록을 반환한다.")
    @Test
    void findMemberIdsByReviewDate() {
        // given
        Member member1 = memberRepository.save(new Member("sample1@sample.com", "sample1"));
        Member member2 = memberRepository.save(new Member("sample2@sample.com", "sample2"));
        Study study1 = studyRepository.save(new Study(StudyType.BOOK, "소프트웨어 장인", "좋은 책", member1));
        Study study2 = studyRepository.save(new Study(StudyType.LECTURE, "김영한의 Spring", "좋은 강의", member2));
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("일일 학습1", LocalDateTime.of(2024, 11, 15, 12, 0), study1));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("일일 학습2", LocalDateTime.of(2024, 11, 15, 12, 0), study2));

        ReviewQueueSave reviewQueueSave1 = new ReviewQueueSave(dailyStudy1.getId(), 5, 1, 2, 3, 4, 5);
        ReviewQueueSave reviewQueueSave2 = new ReviewQueueSave(dailyStudy2.getId(), 5, 1, 2, 3, 4, 5);
        reviewService.save(reviewQueueSave1, member1.getId());
        reviewService.save(reviewQueueSave2, member2.getId());

        // when
        List<Long> memberIds = reviewService.findMemberIdsByReviewDate(LocalDate.of(2024, 11, 18));

        // then
        assertThat(memberIds).hasSize(2)
                .contains(member1.getId(), member2.getId());
    }

}