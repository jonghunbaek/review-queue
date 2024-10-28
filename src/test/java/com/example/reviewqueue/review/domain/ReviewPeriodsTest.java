package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.review.exception.ReviewException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.example.reviewqueue.common.response.ResponseCode.E13001;

class ReviewPeriodsTest {

    @DisplayName("복습 주기의 최솟값인 0미만이면 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("provideMinimumArguments")
    void reviewPeriodMinimumValidationTest(int[] periods, String message) {
        // given
        int firstPeriod = periods[0];
        int secondPeriod = periods[1];
        int thirdPeriod = periods[2];
        int fourthPeriod = periods[3];
        int fifthPeriod = periods[4];

        // when & then
        Assertions.assertThatThrownBy(() -> new ReviewPeriods(firstPeriod, secondPeriod, thirdPeriod, fourthPeriod, fifthPeriod))
            .isInstanceOf(ReviewException.class)
            .hasMessage(message);
    }

    private static Stream<Arguments> provideMinimumArguments() {
        return Stream.of(
            Arguments.of(new int[] {-1, 0, 0, 0, 0}, new ReviewException("firstPeriod :: -1", E13001).getMessage()),
            Arguments.of(new int[] {0, -1, 0, 0, 0}, new ReviewException("secondPeriod :: -1", E13001).getMessage()),
            Arguments.of(new int[] {0, 0, -1, 0, 0}, new ReviewException("thirdPeriod :: -1", E13001).getMessage()),
            Arguments.of(new int[] {0, 0, 0, -1, 0}, new ReviewException("fourthPeriod :: -1", E13001).getMessage()),
            Arguments.of(new int[] {0, 0, 0, 0, -1}, new ReviewException("fifthPeriod :: -1", E13001).getMessage())
        );
    }

    @DisplayName("복습 주기의 최댓값인 14를 초과하면 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("provideMaximumArguments")
    void reviewPeriodMaximumValidationTest(int[] periods, String message) {
        // given
        int firstPeriod = periods[0];
        int secondPeriod = periods[1];
        int thirdPeriod = periods[2];
        int fourthPeriod = periods[3];
        int fifthPeriod = periods[4];

        // when & then
        Assertions.assertThatThrownBy(() -> new ReviewPeriods(firstPeriod, secondPeriod, thirdPeriod, fourthPeriod, fifthPeriod))
            .isInstanceOf(ReviewException.class)
            .hasMessage(message);
    }

    private static Stream<Arguments> provideMaximumArguments() {
        return Stream.of(
            Arguments.of(new int[] {15, 14, 14, 14, 14}, new ReviewException("firstPeriod :: 15", E13001).getMessage()),
            Arguments.of(new int[] {14, 15, 14, 14, 14}, new ReviewException("secondPeriod :: 15", E13001).getMessage()),
            Arguments.of(new int[] {14, 14, 15, 14, 14}, new ReviewException("thirdPeriod :: 15", E13001).getMessage()),
            Arguments.of(new int[] {14, 14, 14, 15, 14}, new ReviewException("fourthPeriod :: 15", E13001).getMessage()),
            Arguments.of(new int[] {14, 14, 14, 14, 15}, new ReviewException("fifthPeriod :: 15", E13001).getMessage())
        );
    }
}