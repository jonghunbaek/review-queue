package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.review.domain.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewData {

    private final long dailyStudyId;
    private final long reviewId;
    private final boolean isLastReview;
    private final List<ReviewKeyword> reviewKeywords;

    private ReviewData(long dailyStudyId, long reviewId, boolean isLastReview, List<ReviewKeyword> reviewKeywords) {
        this.dailyStudyId = dailyStudyId;
        this.reviewId = reviewId;
        this.isLastReview = isLastReview;
        this.reviewKeywords = reviewKeywords;
    }

    public static ReviewData of(Review review) {
        List<ReviewKeyword> reviewKeywords = review.getDailyStudy().getKeywords().stream()
                .map(ReviewKeyword::of)
                .toList();

        return new ReviewData(review.getDailyStudy().getId(), review.getId(), false, reviewKeywords);
    }

    public static ReviewData from(Review review, boolean isLastReview) {
        List<ReviewKeyword> reviewKeywords = review.getDailyStudy().getKeywords().stream()
                .map(ReviewKeyword::of)
                .toList();

        return new ReviewData(review.getDailyStudy().getId(), review.getId(), isLastReview, reviewKeywords);
    }
}
