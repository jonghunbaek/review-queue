package com.example.reviewqueue.dailystudy.service.dto;

import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import lombok.Getter;

@Getter
public class StudyKeywordInfo {

    private final String keyword;
    private final String description;

    private StudyKeywordInfo(String keyword, String description) {
        this.keyword = keyword;
        this.description = description;
    }

    public static StudyKeywordInfo of(StudyKeyword studyKeyword) {
        return new StudyKeywordInfo(studyKeyword.getKeyword(), studyKeyword.getDescription());
    }
}
