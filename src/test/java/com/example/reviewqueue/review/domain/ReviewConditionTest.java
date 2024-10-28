package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.review.exception.ReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewConditionTest {

    @DisplayName("복습 횟수의 범위(1 ~ 5)를 벗어나면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    void reviewTimesValidationTest(int reviewTimes) {
        // when & then
        assertThatThrownBy(() -> new ReviewCondition(reviewTimes, null, null))
            .isInstanceOf(ReviewException.class);
    }
}