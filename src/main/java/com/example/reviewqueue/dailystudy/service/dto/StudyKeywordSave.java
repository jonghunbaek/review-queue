package com.example.reviewqueue.dailystudy.service.dto;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudyKeywordSave {

    private String keyword;
    private String description;

    public StudyKeywordSave(String keyword, String description) {
        this.keyword = keyword;
        this.description = description;
    }

    public StudyKeyword toEntity(DailyStudy dailyStudy) {
        return new StudyKeyword(keyword, description, dailyStudy);
    }
}
