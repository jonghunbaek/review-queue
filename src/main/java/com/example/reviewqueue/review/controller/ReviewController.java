package com.example.reviewqueue.review.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.review.service.ReviewService;
import com.example.reviewqueue.review.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
     *  복습 단일 조회 (읽기 전용)
     */
    @GetMapping("/{reviewId}")
    public ReviewData getreviewData(@PathVariable Long reviewId, @AuthenticatedMember Long memberId) {
        return reviewService.findById(reviewId, memberId);
    }

    /**
     *  복습 완료 처리
     */
    @PatchMapping("/{reviewId}/completion")
    public void completeReview(@PathVariable Long reviewId, @AuthenticatedMember Long memberId) {
        reviewService.completeReview(reviewId, memberId);
    }

    /**
     *  복습 내역 조회 (기간 + 완료 여부 필터링)
     */
    @GetMapping("/history")
    public ReviewHistoriesItem getReviewHistory(
            ReviewHistorySearchCondition condition,
            @PageableDefault(sort = "reviewDate", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticatedMember Long memberId) {
        return reviewService.findReviewHistory(condition, pageable, memberId);
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
