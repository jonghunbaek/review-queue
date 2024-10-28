package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.review.exception.ReviewException;
import com.example.reviewqueue.study.domain.Study;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.reviewqueue.common.response.ResponseCode.E13000;

@Getter
@NoArgsConstructor
@Entity
public class ReviewCondition extends BaseEntity {

    public static final int MIN_REVIEW_TIME = 1;
    public static final int MAX_REVIEW_TIME = 5;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  복습 횟수
     */
    private int reviewTime;

    /**
     *  복습 주기(1일 ~ 14일)
     */
    @Embedded
    private ReviewPeriods reviewPeriods;

    @ManyToOne
    private Study study;

    private ReviewCondition(int reviewTime, int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod, int fifthPeriod, Study study) {
        validateReviewTime(reviewTime);

        this.reviewTime = reviewTime;
        this.reviewPeriods = new ReviewPeriods(firstPeriod, secondPeriod, thirdPeriod, fourthPeriod, fifthPeriod);
        this.study = study;
    }

    private void validateReviewTime(int reviewTime) {
        if (reviewTime < MIN_REVIEW_TIME || reviewTime > MAX_REVIEW_TIME) {
            throw new ReviewException("reviewTime :: " + reviewTime, E13000);
        }
    }

    public static ReviewCondition oneTime(int firstPeriod, Study study) {
        return new ReviewCondition(1, firstPeriod, 0, 0, 0, 0, study);
    }

    public static ReviewCondition twoTimes(int firstPeriod, int secondPeriod, Study study) {
        return new ReviewCondition(2, firstPeriod, secondPeriod, 0, 0, 0,study);
    }

    public static ReviewCondition threeTimes(int firstPeriod, int secondPeriod, int thirdPeriod, Study study) {
        return new ReviewCondition(3, firstPeriod, secondPeriod, thirdPeriod, 0, 0, study);
    }

    public static ReviewCondition fourTimes(int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod, Study study) {
        return new ReviewCondition(4, firstPeriod, secondPeriod, thirdPeriod, fourthPeriod, 0, study);
    }

    public static ReviewCondition fifthTimes(int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod, int fifthPeriod, Study study) {
        return new ReviewCondition(5, firstPeriod, secondPeriod, thirdPeriod, fourthPeriod, fifthPeriod, study);
    }
}
