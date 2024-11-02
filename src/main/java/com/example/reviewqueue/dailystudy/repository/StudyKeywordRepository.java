package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyKeywordRepository extends JpaRepository<StudyKeyword, Long> {

    List<StudyKeyword> findAllByDailyStudyId(Long dailyStudyId);
}
