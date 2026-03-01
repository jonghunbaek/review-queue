package com.example.reviewqueue.review.repository;

import com.example.reviewqueue.common.config.querydsl.QuerydslConfig;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.dto.ReviewHistoryQueryCondition;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyKeywordRepository studyKeywordRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @DisplayName("복습날짜에 해당하는 모든 복습을 조회한다.")
    @Test
    void findAllByReviewDate() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate studyDate1 = LocalDate.of(2024, 10, 31);
        LocalDate studyDate2 = LocalDate.of(2024, 11, 1);
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate1.atTime(0,0), study));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p211-220", studyDate2.atTime(0,0), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword3 = new StudyKeyword("클러스터드 인덱스", "PK 기반의 인덱스", dailyStudy2);
        StudyKeyword keyword4 = new StudyKeyword("세컨더리 인덱스", "PK 이외의 인덱스", dailyStudy2);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2, keyword3, keyword4));

        reviewRepository.save(new Review(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2), dailyStudy1));
        reviewRepository.save(new Review(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2), dailyStudy2));

        // when
        LocalDate targetDate = LocalDate.of(2024, 11, 2);
        List<Review> reviews = reviewRepository.findAllByReviewDate(targetDate);

        // then
        assertThat(reviews).hasSize(2);
    }

    @DisplayName("복습 날짜와 사용자 아이디에 해당하는 모든 복습을 조회한다.")
    @Test
    void findAllByReviewDateAndMemberId() {
        // given
        Member member = memberRepository.save(new Member("test2@email.com", "password", "테스터2"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate studyDate1 = LocalDate.of(2024, 10, 31);
        LocalDate studyDate2 = LocalDate.of(2024, 11, 1);
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate1.atTime(0,0), study));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p211-220", studyDate2.atTime(0,0), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스", dailyStudy1);
        StudyKeyword keyword3 = new StudyKeyword("클러스터드 인덱스", "PK 기반의 인덱스", dailyStudy2);
        StudyKeyword keyword4 = new StudyKeyword("세컨더리 인덱스", "PK 이외의 인덱스", dailyStudy2);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2, keyword3, keyword4));

        reviewRepository.save(new Review(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2), dailyStudy1));
        reviewRepository.save(new Review(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2), dailyStudy2));

        // when
        LocalDate reviewDate = LocalDate.of(2024, 11, 2);
        List<Review> reviews = reviewRepository.findAllByReviewDateAndMemberId(reviewDate, member.getId());

        // then
        assertThat(reviews).hasSize(2);
    }

    @DisplayName("복습날짜에 해당하는 미완료 복습만 조회한다.")
    @Test
    void findAllByReviewDateAndIsCompletedIsFalse() {
        // given
        Member member = memberRepository.save(new Member("test4@email.com", "password", "테스터4"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", LocalDate.of(2024, 10, 31).atTime(0, 0), study));

        LocalDate targetDate = LocalDate.of(2024, 11, 2);
        Review review1 = new Review(LocalDate.of(2024, 11, 1), targetDate, dailyStudy);
        Review review2 = new Review(LocalDate.of(2024, 11, 1), targetDate, dailyStudy);
        Review review3 = new Review(LocalDate.of(2024, 11, 1), targetDate, dailyStudy);
        review1.completeReview();
        reviewRepository.saveAll(List.of(review1, review2, review3));

        // when
        List<Review> reviews = reviewRepository.findAllByReviewDateAndIsCompletedIsFalse(targetDate);

        // then
        assertThat(reviews).hasSize(2);
    }

    @DisplayName("해당 날짜에 미완료 복습이 없으면 빈 리스트를 반환한다.")
    @Test
    void findAllByReviewDateAndIsCompletedIsFalse_allCompleted() {
        // given
        Member member = memberRepository.save(new Member("test5@email.com", "password", "테스터5"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스", LocalDate.of(2024, 10, 31).atTime(0, 0), study));

        LocalDate targetDate = LocalDate.of(2024, 11, 2);
        Review review = new Review(LocalDate.of(2024, 11, 1), targetDate, dailyStudy);
        review.completeReview();
        reviewRepository.save(review);

        // when
        List<Review> reviews = reviewRepository.findAllByReviewDateAndIsCompletedIsFalse(targetDate);

        // then
        assertThat(reviews).isEmpty();
    }

    @DisplayName("기간 내 모든 복습을 조회한다 (isCompleted=null이면 완료/미완료 모두 반환).")
    @Test
    void findAllByHistory_all() {
        // given
        Member member = memberRepository.save(new Member("history1@email.com", "password", "히스토리1"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.of(2024, 11, 1).atTime(0, 0), study));

        LocalDate inRange = LocalDate.of(2024, 11, 10);
        Review completed = new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy);
        Review incomplete = new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy);
        completed.completeReview();
        reviewRepository.saveAll(List.of(completed, incomplete));

        ReviewHistoryQueryCondition condition = new ReviewHistoryQueryCondition(
                member.getId(), null, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewDate").descending());

        // when
        Page<Review> result = reviewRepository.findAllByHistory(condition, pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @DisplayName("isCompleted=true이면 완료된 복습만 반환한다.")
    @Test
    void findAllByHistory_completedOnly() {
        // given
        Member member = memberRepository.save(new Member("history2@email.com", "password", "히스토리2"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.of(2024, 11, 1).atTime(0, 0), study));

        LocalDate inRange = LocalDate.of(2024, 11, 10);
        Review completed = new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy);
        Review incomplete = new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy);
        completed.completeReview();
        reviewRepository.saveAll(List.of(completed, incomplete));

        ReviewHistoryQueryCondition condition = new ReviewHistoryQueryCondition(
                member.getId(), true, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewDate").descending());

        // when
        Page<Review> result = reviewRepository.findAllByHistory(condition, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).isCompleted()).isTrue();
    }

    @DisplayName("isCompleted=false이면 미완료 복습만 반환한다.")
    @Test
    void findAllByHistory_incompleteOnly() {
        // given
        Member member = memberRepository.save(new Member("history3@email.com", "password", "히스토리3"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.of(2024, 11, 1).atTime(0, 0), study));

        LocalDate inRange = LocalDate.of(2024, 11, 10);
        Review completed = new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy);
        Review incomplete = new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy);
        completed.completeReview();
        reviewRepository.saveAll(List.of(completed, incomplete));

        ReviewHistoryQueryCondition condition = new ReviewHistoryQueryCondition(
                member.getId(), false, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewDate").descending());

        // when
        Page<Review> result = reviewRepository.findAllByHistory(condition, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).isCompleted()).isFalse();
    }

    @DisplayName("기간 밖의 복습은 조회되지 않는다.")
    @Test
    void findAllByHistory_outOfRange() {
        // given
        Member member = memberRepository.save(new Member("history4@email.com", "password", "히스토리4"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.of(2024, 10, 1).atTime(0, 0), study));

        reviewRepository.save(new Review(LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 5), dailyStudy));

        ReviewHistoryQueryCondition condition = new ReviewHistoryQueryCondition(
                member.getId(), null, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewDate").descending());

        // when
        Page<Review> result = reviewRepository.findAllByHistory(condition, pageable);

        // then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @DisplayName("다른 멤버의 복습은 조회되지 않는다.")
    @Test
    void findAllByHistory_otherMemberExcluded() {
        // given
        Member member1 = memberRepository.save(new Member("history5a@email.com", "password", "히스토리5A"));
        Member member2 = memberRepository.save(new Member("history5b@email.com", "password", "히스토리5B"));
        Study study1 = studyRepository.save(new Study(StudyType.BOOK, "도서1", "설명", member1));
        Study study2 = studyRepository.save(new Study(StudyType.BOOK, "도서2", "설명", member2));
        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("범위1", LocalDate.of(2024, 11, 1).atTime(0, 0), study1));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("범위2", LocalDate.of(2024, 11, 1).atTime(0, 0), study2));

        LocalDate inRange = LocalDate.of(2024, 11, 10);
        reviewRepository.save(new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy1));
        reviewRepository.save(new Review(LocalDate.of(2024, 11, 9), inRange, dailyStudy2));

        ReviewHistoryQueryCondition condition = new ReviewHistoryQueryCondition(
                member1.getId(), null, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewDate").descending());

        // when
        Page<Review> result = reviewRepository.findAllByHistory(condition, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getMember().getId()).isEqualTo(member1.getId());
    }

    @DisplayName("페이지 크기보다 데이터가 많으면 페이징이 적용된다.")
    @Test
    void findAllByHistory_pagination() {
        // given
        Member member = memberRepository.save(new Member("history6@email.com", "password", "히스토리6"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "테스트 도서", "설명", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위", LocalDate.of(2024, 11, 1).atTime(0, 0), study));

        for (int i = 1; i <= 5; i++) {
            LocalDate reviewDate = LocalDate.of(2024, 11, i + 1);
            reviewRepository.save(new Review(LocalDate.of(2024, 11, i), reviewDate, dailyStudy));
        }

        ReviewHistoryQueryCondition condition = new ReviewHistoryQueryCondition(
                member.getId(), null, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30));
        Pageable pageable = PageRequest.of(0, 3, Sort.by("reviewDate").descending());

        // when
        Page<Review> result = reviewRepository.findAllByHistory(condition, pageable);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
    }

    @DisplayName("일일 학습 아이디로 완료하지 않은 모든 복습을 조회한다.")
    @Test
    void findAllByIsCompletedIsFalseAndDailyStudyId() {
        // given
        Member member = memberRepository.save(new Member("test3@email.com", "password", "테스터3"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate studyDate1 = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate1.atTime(0,0), study));

        Review review1 = new Review(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2), dailyStudy);
        Review review2 = new Review(LocalDate.of(2024, 11, 2), LocalDate.of(2024, 11, 3), dailyStudy);
        Review review3 = new Review(LocalDate.of(2024, 11, 3), LocalDate.of(2024, 11, 4), dailyStudy);
        review1.completeReview();
        review2.completeReview();
        reviewRepository.saveAll(List.of(review1, review2, review3));

        // when
        List<Review> reviews = reviewRepository.findAllByIsCompletedIsFalseAndDailyStudyId(dailyStudy.getId());

        // then
        assertThat(reviews).hasSize(1)
                .extracting("reviewDate")
                .containsExactlyInAnyOrder(
                        review3.getReviewDate()
                );
    }
}
