package com.example.reviewqueue.review.repository;

import com.example.reviewqueue.review.domain.ReviewQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewQueueRepository extends JpaRepository<ReviewQueue, Long> {
}
