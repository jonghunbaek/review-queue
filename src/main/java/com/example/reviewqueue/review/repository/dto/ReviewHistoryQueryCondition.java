package com.example.reviewqueue.review.repository.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReviewHistoryQueryCondition {

    private final Long memberId;
    private final Boolean isCompleted;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ReviewHistoryQueryCondition(Long memberId, Boolean isCompleted, LocalDate startDate, LocalDate endDate) {
        this.memberId = memberId;
        this.isCompleted = isCompleted;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
