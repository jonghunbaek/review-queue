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

    /**
     *  일일 학습에 대한 새로운 복습 데이터를 저장
     */
    @PostMapping
    public void postReviews(@RequestBody ReviewQueueSave reviewQueueSave, @AuthenticatedMember Long memberId) {
        reviewService.save(reviewQueueSave, memberId);
    }

    // TODO :: 일일 학습 아이디와 날짜로 '복습' 데이터를 조회하는 API 작성 예정
    //  데이터 반환시 해당 복습이 마지막 복습인지를 나타내는 필드 필요
}
