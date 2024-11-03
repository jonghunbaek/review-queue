package com.example.reviewqueue.reviewqueue.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.repository.StudyKeywordRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.reviewqueue.domain.ReviewQueue;
import com.example.reviewqueue.reviewqueue.repository.ReviewQueueRepository;
import com.example.reviewqueue.reviewqueue.service.dto.ReviewQueueSave;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewQueueServiceTest {

    @Autowired
    private ReviewQueueService reviewQueueService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private ReviewQueueRepository reviewQueueRepository;

    @DisplayName("복습 조건에 따라 복습할 학습을 저장한다.")
    @Test
    void saveReviewQueue() {
        // given
        Member member = memberRepository.findAll().get(0);
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate startDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("학습 1", startDate.atTime(0,0), study));
        ReviewQueueSave reviewQueueSave = new ReviewQueueSave(dailyStudy.getId(), 5, 1, 2, 3, 4, 5);

        // when
        reviewQueueService.save(reviewQueueSave);
        List<ReviewQueue> reviewQueues = reviewQueueRepository.findAll();

        // then
        assertThat(reviewQueues).hasSize(5)
                .extracting("reviewDate")
                .containsExactlyInAnyOrder(
                        LocalDate.of(2024, 11, 1),
                        LocalDate.of(2024, 11, 3),
                        LocalDate.of(2024, 11, 6),
                        LocalDate.of(2024, 11, 10),
                        LocalDate.of(2024, 11, 15)
                );
    }
}