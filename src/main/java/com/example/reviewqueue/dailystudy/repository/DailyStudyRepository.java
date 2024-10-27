package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStudyRepository extends JpaRepository<DailyStudy, Long> {
}
