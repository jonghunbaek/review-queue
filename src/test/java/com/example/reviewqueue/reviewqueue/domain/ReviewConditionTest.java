package com.example.reviewqueue.reviewqueue.domain;

import com.example.reviewqueue.reviewqueue.exception.ReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewConditionTest {

    @DisplayName("복습 횟수와 파라미터로 전달되는 복습 주기의 개수가 다르면 예외를 발생시킨다.")
    @Test
    void compareReviewTimesWithReviewPeriods() {
        // given
        List<Integer> reviewPeriods = List.of(1,2,3);
        int reviewTimes = 4;

        // when & then
        assertThatThrownBy(() -> new ReviewCondition(reviewTimes, reviewPeriods, null))
                .isInstanceOf(ReviewException.class)
                .hasMessage(E13002.getMessage());
    }

    @DisplayName("복습 횟수의 범위가 0 ~ 5회를 벗어나면 예외를 발생시킨다.(0미만인 경우는 복습 횟수, 복습 주기 개수 비교에서 이미 검증됨)")
    @Test
    void validateReviewTimesRange() {
        assertThatThrownBy(() -> new ReviewCondition(6, List.of(1,2,3,4,5,6), null))
                .isInstanceOf(ReviewException.class)
                .hasMessage(E13000.getMessage());
    }

    @DisplayName("복습 주기의 범위(1 ~ 14일)을 벗어나면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 15})
    void validateReviewPeriodRange(int reviewPeriod) {
        List<Integer> periods = List.of(reviewPeriod);

        assertThatThrownBy(() -> new ReviewCondition(1, periods, null))
                .isInstanceOf(ReviewException.class)
                .hasMessage(E13001.getMessage());
    }
}