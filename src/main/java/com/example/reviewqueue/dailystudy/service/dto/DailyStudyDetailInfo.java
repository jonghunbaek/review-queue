package com.example.reviewqueue.dailystudy.service.dto;

import lombok.Getter;

@Getter
public class DailyStudyDetailInfo {

    private final DailyStudyGeneralInfo dailyStudyGeneralInfo;

    public DailyStudyDetailInfo(DailyStudyGeneralInfo dailyStudyGeneralInfo) {
        this.dailyStudyGeneralInfo = dailyStudyGeneralInfo;
    }
}
