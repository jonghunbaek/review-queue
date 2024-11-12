package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.review.domain.ReviewCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
@Getter
public class ReviewQueueSave {

    private Long dailyStudyId;
    private Integer reviewTimes;
    private int firstPeriod;
    private int secondPeriod;
    private int thirdPeriod;
    private int fourthPeriod;
    private int fifthPeriod;

    public ReviewQueueSave(Long dailyStudyId, Integer reviewTimes, int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod, int fifthPeriod) {
        this.dailyStudyId = dailyStudyId;
        this.reviewTimes = reviewTimes;
        this.firstPeriod = firstPeriod;
        this.secondPeriod = secondPeriod;
        this.thirdPeriod = thirdPeriod;
        this.fourthPeriod = fourthPeriod;
        this.fifthPeriod = fifthPeriod;
    }

    public ReviewCondition toReviewCondition(DailyStudy dailyStudy) {
        List<Integer> periods = Stream.of(firstPeriod, secondPeriod, thirdPeriod, fourthPeriod, fifthPeriod)
                .filter(period -> period > 0)
                .toList();

        return new ReviewCondition(reviewTimes, periods, dailyStudy);
    }
}
