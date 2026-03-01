package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.review.repository.dto.ReviewHistoryQueryCondition;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReviewHistorySearchCondition {

    private final Boolean isCompleted;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ReviewHistorySearchCondition(Boolean isCompleted, LocalDate startDate, LocalDate endDate) {
        this.isCompleted = isCompleted;
        this.startDate = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        this.endDate = endDate != null ? endDate : LocalDate.now().plusMonths(1);
    }

    public ReviewHistoryQueryCondition toQueryCondition(Long memberId) {
        return new ReviewHistoryQueryCondition(memberId, isCompleted, startDate, endDate);
    }
}
