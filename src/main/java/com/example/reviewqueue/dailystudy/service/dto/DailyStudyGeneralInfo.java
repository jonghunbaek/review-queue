package com.example.reviewqueue.dailystudy.service.dto;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class DailyStudyGeneralInfo {

    private Long dailyStudyId;
    private String studyRange;
    private LocalDateTime studyDateTime;

    private DailyStudyGeneralInfo(Long dailyStudyId, String studyRange, LocalDateTime studyDateTime) {
        this.dailyStudyId = dailyStudyId;
        this.studyRange = studyRange;
        this.studyDateTime = studyDateTime;
    }

    public static DailyStudyGeneralInfo of(DailyStudy dailyStudy) {
        return new DailyStudyGeneralInfo(dailyStudy.getId(), dailyStudy.getStudyRange(), dailyStudy.getStudyDateTime());
    }
}
