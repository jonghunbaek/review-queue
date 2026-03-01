package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.study.domain.StudyType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ReviewHistoryItem {

    private final Long reviewId;
    private final LocalDate reviewDate;
    private final LocalDate previousReviewDate;
    private final boolean isCompleted;
    private final String studyTitle;
    private final StudyType studyType;
    private final List<ReviewKeyword> keywords;

    public ReviewHistoryItem(Long reviewId, LocalDate reviewDate, LocalDate previousReviewDate,
                             boolean isCompleted, String studyTitle, StudyType studyType,
                             List<ReviewKeyword> keywords) {
        this.reviewId = reviewId;
        this.reviewDate = reviewDate;
        this.previousReviewDate = previousReviewDate;
        this.isCompleted = isCompleted;
        this.studyTitle = studyTitle;
        this.studyType = studyType;
        this.keywords = keywords;
    }

    public static ReviewHistoryItem of(Review review) {
        List<ReviewKeyword> keywords = review.getDailyStudy().getKeywords().stream()
                .map(ReviewKeyword::of)
                .toList();
        return new ReviewHistoryItem(
                review.getId(),
                review.getReviewDate(),
                review.getPreviousReviewDate(),
                review.isCompleted(),
                review.getDailyStudy().getStudy().getTitle(),
                review.getDailyStudy().getStudy().getStudyType(),
                keywords
        );
    }
}
