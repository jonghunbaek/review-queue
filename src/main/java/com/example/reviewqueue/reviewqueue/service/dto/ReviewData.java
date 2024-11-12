package com.example.reviewqueue.reviewqueue.service.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewData {

    private Long userId;
    private List<ReviewKeyword> reviewKeywords;

    public ReviewData(Long userId, List<ReviewKeyword> reviewKeywords) {
        this.userId = userId;
        this.reviewKeywords = reviewKeywords;
    }
}
