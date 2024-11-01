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

    private Integer reviewTimes;
    
    private List<Integer> periods;
    
    private DailyStudy dailyStudy;

    // TODO :: 검증기 추가 필요함
    public ReviewCondition(Integer reviewTimes, List<Integer> periods, DailyStudy dailyStudy) {
        validateReviewTimes(reviewTimes, periods);

        this.dailyStudy = dailyStudy;
        this.reviewTimes = reviewTimes;
        this.periods = periods;
    }

    private void validateReviewTimes(Integer reviewTimes, List<Integer> periods) {
        if (reviewTimes != periods.size()) {
            throw new ReviewException(ResponseCode.E13000);
        }
    }
}
