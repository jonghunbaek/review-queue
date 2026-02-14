package com.example.reviewqueue.review.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.review.service.ReviewService;
import com.example.reviewqueue.review.service.dto.ReviewData;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController implements ReviewApi {

    private final ReviewService reviewService;

    /**
     *  일일 학습에 대한 새로운 복습 데이터를 저장
     */
    @PostMapping
    public void postReviews(@RequestBody ReviewQueueSave reviewQueueSave, @AuthenticatedMember Long memberId) {
        reviewService.save(reviewQueueSave, memberId);
    }

    /**
     *  복습 단일 조회(마지막 복습 여부 포함), 완료처리
     */
    @GetMapping("/{reviewId}")
    public ReviewData getreviewData(@PathVariable Long reviewId, @AuthenticatedMember Long memberId) {
        return reviewService.findById(reviewId, memberId);
    }

    /**
     *  조건에 해당하는 복습 자료를 모두 조회
     */
    @GetMapping
    public ReviewsData getReviewsData(
            @RequestParam(required = false) Long dailyStudyId,
            @RequestParam(required = false) LocalDate reviewDate,
            @AuthenticatedMember Long memberId) {

        return reviewService.findAllReviewDataBy(reviewDate, dailyStudyId, memberId);
    }
}
