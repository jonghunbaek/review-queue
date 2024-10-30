package com.example.reviewqueue.study.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Study extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  스터디 종류
     */
    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    /**
     *  스터디 제목
     */
    private String title;

    /**
     *  스터디 내용 요약
     */
    private String description;

    @ManyToOne
    private Member member;

    public Study(StudyType studyType, String title, String description, Member member) {
        this.studyType = studyType;
        this.title = title;
        this.description = description;
        this.member = member;
    }
}
