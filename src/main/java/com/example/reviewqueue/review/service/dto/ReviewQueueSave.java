package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.review.domain.ReviewCondition;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ReviewQueueSave {

    private Long dailyStudyId;

    @Size(max = 10)
    private List<Integer> periods;

    public ReviewQueueSave(Long dailyStudyId, List<Integer> periods) {
        this.dailyStudyId = dailyStudyId;
        this.periods = periods;
    }

    public ReviewCondition toReviewCondition(DailyStudy dailyStudy) {
        return new ReviewCondition(periods, dailyStudy);
    }
}
