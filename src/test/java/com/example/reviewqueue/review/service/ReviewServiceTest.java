package com.example.reviewqueue.review.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
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
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @DisplayName("복습 조건에 따라 복습할 학습을 저장한다.")
    @Test
    void saveReviewQueue() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate startDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("학습 1", startDate.atTime(0,0), study));
        ReviewQueueSave reviewQueueSave = new ReviewQueueSave(dailyStudy.getId(), 5, 1, 2, 3, 4, 5);

        // when
        reviewService.save(reviewQueueSave, member.getId());
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

}
