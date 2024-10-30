package com.example.reviewqueue.study.service.dto;

import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudySave {

    private StudyType studyType;
    private String title;
    private String description;

    public StudySave(StudyType studyType, String title, String description) {
        this.studyType = studyType;
        this.title = title;
        this.description = description;
    }

    public Study toEntity(Member member) {
        return new Study(studyType, title, description, member);
    }
}
