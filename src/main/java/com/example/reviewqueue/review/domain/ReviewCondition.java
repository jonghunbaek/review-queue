package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.review.exception.ReviewException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.reviewqueue.common.response.ResponseCode.E13000;
import static com.example.reviewqueue.common.response.ResponseCode.E13001;

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
     *  복습 주기(1 ~ 5회)
     */
    private Integer firstPeriod;

    private Integer secondPeriod;

    private Integer thirdPeriod;

    private Integer fourthPeriod;

    public ReviewCondition(int reviewTime, int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod) throws ReviewException {
        validateReviewTime(reviewTime);
        validatePeriod(firstPeriod, secondPeriod, thirdPeriod, fourthPeriod);

        this.reviewTime = reviewTime;
        this.firstPeriod = firstPeriod;
        this.secondPeriod = secondPeriod;
        this.thirdPeriod = thirdPeriod;
        this.fourthPeriod = fourthPeriod;
    }

    private static void validateReviewTime(int reviewTime) {
        if (reviewTime < MIN_REVIEW_TIME || reviewTime > MAX_REVIEW_TIME) {
            throw new ReviewException("reviewTime :: " + reviewTime, E13000);
        }
    }

    // TODO :: 범위 예외 처리 필요
    private void validatePeriod(int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod) {
        if (firstPeriod < 0 || firstPeriod > 14) {
            throw new ReviewException("firstPeriod :: " + firstPeriod, E13001);
        }
    }
}
