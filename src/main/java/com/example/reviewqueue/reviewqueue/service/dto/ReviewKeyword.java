package com.example.reviewqueue.reviewqueue.service.dto;

import lombok.Getter;

@Getter
public class ReviewKeyword {

    private String keyword;
    private String description;

    public ReviewKeyword(String keyword, String description) {
        this.keyword = keyword;
        this.description = description;
    }
}
