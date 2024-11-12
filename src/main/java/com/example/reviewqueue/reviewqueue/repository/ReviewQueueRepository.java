package com.example.reviewqueue.reviewqueue.repository;

import com.example.reviewqueue.reviewqueue.domain.ReviewQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReviewQueueRepository extends JpaRepository<ReviewQueue, Long> {

    List<ReviewQueue> findAllByReviewDate(LocalDate reviewDate);
}
