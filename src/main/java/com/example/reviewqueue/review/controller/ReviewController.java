package com.example.reviewqueue.review.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.review.service.ReviewService;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public void postReviews(@RequestBody ReviewQueueSave reviewQueueSave, @AuthenticatedMember Long memberId) {
        reviewService.save(reviewQueueSave, memberId);
    }
}
