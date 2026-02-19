package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.common.response.ResponseCode;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.review.exception.ReviewException;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewCondition {

    public static final int MAX_REVIEW_TIMES = 10;
    public static final int MIN_REVIEW_TIMES = 0;
    public static final int MAX_REVIEW_PERIOD = 60;
    public static final int MIN_REVIEW_PERIOD = 1;

    private final List<Integer> reviewPeriods;
    private final DailyStudy dailyStudy;

    public ReviewCondition(List<Integer> reviewPeriods, DailyStudy dailyStudy) {
        validateReviewTimes(reviewPeriods);
        validateReviewPeriods(reviewPeriods);

        this.reviewPeriods = reviewPeriods;
        this.dailyStudy = dailyStudy;
    }

    private void validateReviewTimes(List<Integer> periods) {
        if (periods.size() < MIN_REVIEW_TIMES || periods.size() > MAX_REVIEW_TIMES) {
            throw new ReviewException(ResponseCode.E13000);
        }
    }

    private void validateReviewPeriods(List<Integer> periods) {
        boolean isPresentIncorrectPeriod = periods.stream()
                .anyMatch(period -> period < MIN_REVIEW_PERIOD || period > MAX_REVIEW_PERIOD);

        if (isPresentIncorrectPeriod) {
            throw new ReviewException(ResponseCode.E13001);
        }
    }
}
