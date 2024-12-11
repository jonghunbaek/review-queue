package com.example.reviewqueue.studykeyword.repository;

import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyKeywordRepository extends JpaRepository<StudyKeyword, Long> {

    List<StudyKeyword> findAllByDailyStudyId(Long dailyStudyId);
}
