package com.example.reviewqueue.study.service.dto;

import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import lombok.Getter;

@Getter
public class StudyInfo {

    private final Long studyId;
    private final StudyType studyType;
    private final String title;
    private final String description;

    private StudyInfo(Long studyId, StudyType studyType, String title, String description) {
        this.studyId = studyId;
        this.studyType = studyType;
        this.title = title;
        this.description = description;
    }

    public static StudyInfo of(Study study) {
        return new StudyInfo(study.getId(), study.getStudyType(), study.getTitle(), study.getDescription());
    }
}
