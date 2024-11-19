package com.example.reviewqueue.review.service.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ReviewsData {

    private final long memberId;
    private final LocalDate reviewDate;
    private final List<ReviewData> reviews;

    public ReviewsData(long memberId, LocalDate reviewDate, List<ReviewData> reviews) {
        this.memberId = memberId;
        this.reviewDate = reviewDate;
        this.reviews = reviews;
    }
}
