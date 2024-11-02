package com.example.reviewqueue.dailystudy.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  학습 키워드
     */
    private String keyword;

    /**
     *  학습 키워드 설명
     */
    private String description;

    @ManyToOne
    private DailyStudy dailyStudy;

    public StudyKeyword(String keyword, String description, DailyStudy dailyStudy) {
        this.keyword = keyword;
        this.description = description;
        this.dailyStudy = dailyStudy;
    }
}
