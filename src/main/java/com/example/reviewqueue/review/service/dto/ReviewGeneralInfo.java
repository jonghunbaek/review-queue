package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.review.domain.Review;

public class ReviewGeneralInfo {

    private final Long reviewId;
    private final String studyTitle;

    private ReviewGeneralInfo(Long reviewId, String studyTitle) {
        this.reviewId = reviewId;
        this.studyTitle = studyTitle;
    }

    public static ReviewGeneralInfo of(Review review) {
        String studyTitle = review.getDailyStudy().getStudy().getTitle();

        return new ReviewGeneralInfo(review.getId(), studyTitle);
    }
}
