package com.example.reviewqueue.dailystudy.service.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DailyStudyDetailInfo {

    private final DailyStudyGeneralInfo dailyStudy;
    private final List<StudyKeywordInfo> studyKeywords;

    public DailyStudyDetailInfo(DailyStudyGeneralInfo dailyStudy, List<StudyKeywordInfo> studyKeywords) {
        this.dailyStudy = dailyStudy;
        this.studyKeywords = studyKeywords;
    }
}
