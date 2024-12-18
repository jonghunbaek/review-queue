package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;

import java.time.LocalDate;
import java.util.List;

public interface DailyStudyQueryDslRepository {

    List<DailyStudy> findAllByCondition(Long studyId, LocalDate startDate, LocalDate endDate, boolean createDateSortDesc);
}
