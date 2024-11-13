package com.example.reviewqueue.review.service.dto;

import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import lombok.Getter;

@Getter
public class ReviewKeyword {

    private String keyword;
    private String description;

    public ReviewKeyword(String keyword, String description) {
        this.keyword = keyword;
        this.description = description;
    }

    public static ReviewKeyword of(StudyKeyword keyword) {
        return new ReviewKeyword(keyword.getKeyword(), keyword.getDescription());
    }
}
