package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import com.example.reviewqueue.review.domain.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewData {

    private Long userId;
    private List<ReviewKeyword> reviewKeywords;

    public ReviewData(Long userId, List<ReviewKeyword> reviewKeywords) {
        this.userId = userId;
        this.reviewKeywords = reviewKeywords;
    }

    public static ReviewData of(Review review) {
        List<StudyKeyword> keywords = review.getDailyStudy().getKeywords();
        List<ReviewKeyword> reviewKeywords = keywords.stream()
                .map(ReviewKeyword::of)
                .toList();

        return new ReviewData(review.getMember().getId(), reviewKeywords);
    }
}
