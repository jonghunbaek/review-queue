package com.example.reviewqueue.review.repository;

import com.example.reviewqueue.review.domain.ReviewCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewConditionRepository extends JpaRepository<ReviewCondition, Long> {
}
