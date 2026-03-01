package com.example.reviewqueue.review.controller;

import com.example.reviewqueue.review.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "복습", description = "복습 등록 및 조회")
public interface ReviewApi {

    @Operation(summary = "복습 등록", security = @SecurityRequirement(name = "Bearer Authentication"))
    void postReviews(ReviewQueueSave reviewQueueSave, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "복습 단일 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    ReviewData getreviewData(@Parameter(description = "복습 ID") Long reviewId, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "복습 완료 처리", security = @SecurityRequirement(name = "Bearer Authentication"))
    void completeReview(@Parameter(description = "복습 ID") Long reviewId, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "복습 내역 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    ReviewHistoriesItem getReviewHistory(@Parameter(description = "검색 조건") ReviewHistorySearchCondition condition, @Parameter Pageable pageable, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "복습 전체 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    ReviewsData getReviewsData(@Parameter(description = "일일 학습 ID") Long dailyStudyId, @Parameter(description = "복습 날짜") LocalDate reviewDate, @Parameter(hidden = true) Long memberId);
}
