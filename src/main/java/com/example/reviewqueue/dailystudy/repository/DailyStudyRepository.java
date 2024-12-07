package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyStudyRepository extends JpaRepository<DailyStudy, Long> {

    List<DailyStudy> findAllByStudyId(Long studyId);
}
