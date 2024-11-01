package com.example.reviewqueue.reviewqueue.service.dto;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.reviewqueue.domain.ReviewCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ReviewQueueSave {

    private Long dailyStudyId;
    private Integer reviewTimes;
    // TODO :: 순서가 뒤바뀌는 것에 대한 검증은 어떻게?
    private List<Integer> periods;

    public ReviewQueueSave(Long dailyStudyId, Integer reviewTimes, List<Integer> periods) {
        this.dailyStudyId = dailyStudyId;
        this.reviewTimes = reviewTimes;
        this.periods = periods;
    }

    public ReviewCondition toReviewCondition(DailyStudy dailyStudy) {
        return new ReviewCondition(reviewTimes, periods, dailyStudy);
    }
}
