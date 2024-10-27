package com.example.reviewqueue.study.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Study extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    private String title;

    private String description;

    @ManyToOne
    private Member member;

    public Study(StudyType studyType, String title, String description) {
        this.studyType = studyType;
        this.title = title;
        this.description = description;
    }
}
