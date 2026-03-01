package com.example.reviewqueue.review.repository;

import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.dto.ReviewHistoryQueryCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReviewQueryDslRepository {

    Page<Review> findAllByHistory(ReviewHistoryQueryCondition condition, Pageable pageable);
}
