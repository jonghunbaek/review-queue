package com.example.reviewqueue.dailystudy.service.dto;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.study.domain.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class DailyStudySave {

    private Long studyId;
    private String studyRange;

    public DailyStudySave(Long studyId, String studyRange) {
        this.studyId = studyId;
        this.studyRange = studyRange;
    }

    public DailyStudy toEntity(Study study) {
        return new DailyStudy(studyRange, LocalDateTime.now(), study);
    }
}
