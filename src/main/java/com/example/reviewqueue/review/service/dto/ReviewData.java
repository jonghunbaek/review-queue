package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.review.domain.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewData {

    private final long dailyStudyId;
    private final List<ReviewKeyword> reviewKeywords;

    public ReviewData(long dailyStudyId, List<ReviewKeyword> reviewKeywords) {
        this.dailyStudyId = dailyStudyId;
        this.reviewKeywords = reviewKeywords;
    }

    public static ReviewData of(Review review) {
        List<ReviewKeyword> reviewKeywords = review.getDailyStudy().getKeywords().stream()
                .map(ReviewKeyword::of)
                .toList();

        return new ReviewData(review.getDailyStudy().getId(), reviewKeywords);
    }
}
