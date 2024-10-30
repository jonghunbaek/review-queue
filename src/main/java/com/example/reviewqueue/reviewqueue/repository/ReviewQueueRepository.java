package com.example.reviewqueue.reviewqueue.repository;

import com.example.reviewqueue.reviewqueue.domain.ReviewQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewQueueRepository extends JpaRepository<ReviewQueue, Long> {
}
