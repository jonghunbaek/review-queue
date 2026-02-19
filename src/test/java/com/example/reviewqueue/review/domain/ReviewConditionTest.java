package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.review.exception.ReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewConditionTest {

    @DisplayName("복습 횟수의 범위가 0 ~ 10회를 벗어나면 예외를 발생시킨다.")
    @Test
    void validateReviewTimesRange() {
        assertThatThrownBy(() -> new ReviewCondition(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), null))
                .isInstanceOf(ReviewException.class)
                .hasMessage(E13000.getMessage());
    }

    @DisplayName("복습 주기의 범위(1 ~ 60일)을 벗어나면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 61})
    void validateReviewPeriodRange(int reviewPeriod) {
        List<Integer> periods = List.of(reviewPeriod);

        assertThatThrownBy(() -> new ReviewCondition(periods, null))
                .isInstanceOf(ReviewException.class)
                .hasMessage(E13001.getMessage());
    }
}
