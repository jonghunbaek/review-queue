package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.review.exception.ReviewException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.reviewqueue.common.response.ResponseCode.E13001;

@Getter
@NoArgsConstructor
@Embeddable
public class ReviewPeriods {

    public static final int REVIEW_PERIOD_MIN = 0;
    public static final int REVIEW_PERIOD_MAX = 14;

    private int firstPeriod;
    private int secondPeriod;
    private int thirdPeriod;
    private int fourthPeriod;
    private int fifthPeriod;

    public ReviewPeriods(int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod, int fifthPeriod) {
        validateReviewPeriod(firstPeriod, secondPeriod, thirdPeriod, fourthPeriod, fifthPeriod);

        this.firstPeriod = firstPeriod;
        this.secondPeriod = secondPeriod;
        this.thirdPeriod = thirdPeriod;
        this.fourthPeriod = fourthPeriod;
        this.fifthPeriod = fifthPeriod;
    }

    private void validateReviewPeriod(int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod, int fifthPeriod) {
        if (firstPeriod < REVIEW_PERIOD_MIN || firstPeriod > REVIEW_PERIOD_MAX) {
            throw new ReviewException("firstPeriod :: " + firstPeriod, E13001);
        }

        if (secondPeriod < REVIEW_PERIOD_MIN || secondPeriod > REVIEW_PERIOD_MAX) {
            throw new ReviewException("secondPeriod :: " + secondPeriod, E13001);
        }

        if (thirdPeriod < REVIEW_PERIOD_MIN || thirdPeriod > REVIEW_PERIOD_MAX) {
            throw new ReviewException("thirdPeriod :: " + thirdPeriod, E13001);
        }

        if (fourthPeriod < REVIEW_PERIOD_MIN || fourthPeriod > REVIEW_PERIOD_MAX) {
            throw new ReviewException("fourthPeriod :: " + fourthPeriod, E13001);
        }

        if (fifthPeriod < REVIEW_PERIOD_MIN || fifthPeriod > REVIEW_PERIOD_MAX) {
            throw new ReviewException("fifthPeriod :: " + fifthPeriod, E13001);
        }
    }
}
