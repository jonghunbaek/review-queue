package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyStudyRepository extends JpaRepository<DailyStudy, Long>, DailyStudyQueryDslRepository {

    List<DailyStudy> findAllByStudyId(Long studyId);
}
