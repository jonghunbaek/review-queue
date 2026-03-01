package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.common.domain.Pagination;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewHistoriesItem {

    private final List<ReviewHistoryItem> histories;
    private final Pagination pagination;

    public ReviewHistoriesItem(List<ReviewHistoryItem> histories, Pagination pagination) {
        this.histories = histories;
        this.pagination = pagination;
    }
}
