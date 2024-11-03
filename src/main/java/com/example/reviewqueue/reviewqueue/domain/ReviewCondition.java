package com.example.reviewqueue.reviewqueue.domain;

import com.example.reviewqueue.common.response.ResponseCode;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.reviewqueue.exception.ReviewException;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewCondition {

    public static final int MAX_REVIEW_TIMES = 5;
    public static final int MIN_REVIEW_TIMES = 0;
    public static final int MAX_REVIEW_PERIOD = 14;
    public static final int MIN_REVIEW_PERIOD = 1;

    private int reviewTimes;
    
    private List<Integer> periods;
    
    private DailyStudy dailyStudy;

    public ReviewCondition(int reviewTimes, List<Integer> periods, DailyStudy dailyStudy) {
        validateReviewTimes(reviewTimes, periods);
        validateReviewPeriods(periods);

        this.reviewTimes = reviewTimes;
        this.periods = periods;
        this.dailyStudy = dailyStudy;
    }

    private void validateReviewTimes(int reviewTimes, List<Integer> periods) {
        if (reviewTimes != periods.size()) {
            throw new ReviewException(ResponseCode.E13002);
        }

        if (reviewTimes < MIN_REVIEW_TIMES || reviewTimes > MAX_REVIEW_TIMES) {
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
