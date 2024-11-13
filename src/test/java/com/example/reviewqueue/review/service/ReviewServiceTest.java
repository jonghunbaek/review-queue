package com.example.reviewqueue.review.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.repository.StudyKeywordRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.review.service.dto.ReviewData;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

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
        reviewService.save(reviewQueueSave);
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
        Study study = studyRepository.findAll().get(0);
        LocalDate studyDate1 = LocalDate.of(2024, 10, 31);
        LocalDate studyDate2 = LocalDate.of(2024, 11, 1);
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate1.atTime(0,0), study));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p211-220", studyDate2.atTime(0,0), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword3 = new StudyKeyword("클러스터드 인덱스", "PK 기반의 인덱스", dailyStudy2);
        StudyKeyword keyword4 = new StudyKeyword("세컨더리 인덱스", "PK 이외의 인덱스", dailyStudy2);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2, keyword3, keyword4));

        ReviewQueueSave reviewQueueSave1 = new ReviewQueueSave(dailyStudy1.getId(), 5, 1, 2, 3, 4, 5);
        ReviewQueueSave reviewQueueSave2 = new ReviewQueueSave(dailyStudy2.getId(), 5, 1, 2, 3, 4, 5);
        reviewService.save(reviewQueueSave1);
        reviewService.save(reviewQueueSave2);

        // when
        LocalDate targetDate = LocalDate.of(2024, 11, 2);
        List<ReviewData> reviewDatas = reviewService.findAllReviewDataByDate(targetDate);

        // then
        assertAll(
                () -> assertThat(reviewDatas).hasSize(2),
                () -> assertThat(reviewDatas.get(0).getReviewKeywords()).hasSize(2)
                        .extracting("keyword")
                        .containsExactlyInAnyOrder(
                                tuple("B-Tree 인덱스"),
                                tuple("R-Tree 인덱스")
                        ),
                () -> assertThat(reviewDatas.get(1).getReviewKeywords()).hasSize(2)
                        .extracting("keyword")
                        .containsExactlyInAnyOrder(
                                tuple("클러스터드 인덱스"),
                                tuple("세컨더리 인덱스")
                        )
        );
    }

}