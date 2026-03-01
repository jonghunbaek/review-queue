package com.example.reviewqueue.review.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.review.service.dto.ReviewHistoriesItem;
import com.example.reviewqueue.review.service.dto.ReviewHistorySearchCondition;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private StudyKeywordRepository studyKeywordRepository;

    @DisplayName("복습 조건에 따라 복습할 학습을 저장한다.")
    @Test
    void saveReviewQueue() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate startDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("학습 1", startDate.atTime(0,0), study));
        ReviewQueueSave reviewQueueSave = new ReviewQueueSave(dailyStudy.getId(), List.of(1, 2, 3, 4, 5));

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

    @DisplayName("startDate/endDate가 null이면 기본값(오늘 ±1달)이 적용된다.")
    @Test
    void findReviewHistory_defaultDateRange() {
        // given
        ReviewHistorySearchCondition condition = new ReviewHistorySearchCondition(null, null, null);

        // then
        assertThat(condition.getStartDate()).isEqualTo(LocalDate.now().minusMonths(1));
        assertThat(condition.getEndDate()).isEqualTo(LocalDate.now().plusMonths(1));
    }

    @DisplayName("isCompleted=true이면 완료된 복습만 반환한다.")
    @Test
    void findReviewHistory_completedOnly() {
        // given
        Member member = memberRepository.save(new Member("history_svc1@email.com", "password", "서비스히스토리1"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.now().atTime(0, 0), study));
        studyKeywordRepository.save(new StudyKeyword("키워드", "설명", dailyStudy));

        LocalDate today = LocalDate.now();
        Review completed = new Review(today.minusDays(1), today, dailyStudy);
        Review incomplete = new Review(today.minusDays(1), today, dailyStudy);
        completed.completeReview();
        reviewRepository.saveAll(List.of(completed, incomplete));

        ReviewHistorySearchCondition condition = new ReviewHistorySearchCondition(true, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewDate").descending());

        // when
        ReviewHistoriesItem result = reviewService.findReviewHistory(condition, pageable, member.getId());

        // then
        assertThat(result.getHistories()).hasSize(1);
        assertThat(result.getHistories().get(0).isCompleted()).isTrue();
        assertThat(result.getPagination().getTotalRows()).isEqualTo(1);
    }

    @DisplayName("isCompleted=false이면 미완료 복습만 반환한다.")
    @Test
    void findReviewHistory_incompleteOnly() {
        // given
        Member member = memberRepository.save(new Member("history_svc2@email.com", "password", "서비스히스토리2"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.now().atTime(0, 0), study));
        studyKeywordRepository.save(new StudyKeyword("키워드", "설명", dailyStudy));

        LocalDate today = LocalDate.now();
        Review completed = new Review(today.minusDays(1), today, dailyStudy);
        Review incomplete = new Review(today.minusDays(1), today, dailyStudy);
        completed.completeReview();
        reviewRepository.saveAll(List.of(completed, incomplete));

        ReviewHistorySearchCondition condition = new ReviewHistorySearchCondition(false, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewDate").descending());

        // when
        ReviewHistoriesItem result = reviewService.findReviewHistory(condition, pageable, member.getId());

        // then
        assertThat(result.getHistories()).hasSize(1);
        assertThat(result.getHistories().get(0).isCompleted()).isFalse();
        assertThat(result.getPagination().getTotalRows()).isEqualTo(1);
    }

    @DisplayName("페이징 정보가 응답에 포함된다.")
    @Test
    void findReviewHistory_paginationInfo() {
        // given
        Member member = memberRepository.save(new Member("history_svc3@email.com", "password", "서비스히스토리3"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.now().atTime(0, 0), study));
        studyKeywordRepository.save(new StudyKeyword("키워드", "설명", dailyStudy));

        LocalDate today = LocalDate.now();
        for (int i = 1; i <= 5; i++) {
            reviewRepository.save(new Review(today.minusDays(i + 1), today.minusDays(i), dailyStudy));
        }

        ReviewHistorySearchCondition condition = new ReviewHistorySearchCondition(null, null, null);
        Pageable pageable = PageRequest.of(0, 3, Sort.by("reviewDate").descending());

        // when
        ReviewHistoriesItem result = reviewService.findReviewHistory(condition, pageable, member.getId());

        // then
        assertThat(result.getHistories()).hasSize(3);
        assertThat(result.getPagination().getTotalRows()).isEqualTo(5);
        assertThat(result.getPagination().getTotalPages()).isEqualTo(2);
        assertThat(result.getPagination().getIsFirst()).isTrue();
        assertThat(result.getPagination().getIsLast()).isFalse();
    }
}
