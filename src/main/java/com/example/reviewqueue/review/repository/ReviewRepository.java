package com.example.reviewqueue.review.repository;

import com.example.reviewqueue.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByReviewDate(LocalDate reviewDate);
}
