package com.example.reviewqueue.dailystudy.service.dto;

import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import lombok.Getter;

@Getter
public class StudyKeywordInfo {

    private final Long studyKeywordId;
    private final String keyword;
    private final String description;

    private StudyKeywordInfo(Long studyKeywordId, String keyword, String description) {
        this.studyKeywordId = studyKeywordId;
        this.keyword = keyword;
        this.description = description;
    }

    public static StudyKeywordInfo of(StudyKeyword studyKeyword) {
        return new StudyKeywordInfo(studyKeyword.getId(), studyKeyword.getKeyword(), studyKeyword.getDescription());
    }
}
