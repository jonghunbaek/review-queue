package com.example.reviewqueue.review.repository;

import com.example.reviewqueue.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndIsCompletedIsFalse(Long reviewId);
    List<Review> findAllByDailyStudyId(Long dailyStudyId);
    List<Review> findAllByReviewDate(LocalDate reviewDate);
    List<Review> findAllByReviewDateAndMemberId(LocalDate reviewDate, Long memberId);
    List<Review> findAllByReviewDateAndDailyStudyIdAndMemberId(LocalDate reviewDate, Long dailyStudyId, Long memberId);
}
