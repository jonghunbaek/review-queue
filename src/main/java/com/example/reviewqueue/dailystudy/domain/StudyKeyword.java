package com.example.reviewqueue.dailystudy.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class StudyKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  학습 키워드
     */
    private String keywords;

    /**
     *  학습 키워드 설명
     */
    private String description;

    @ManyToOne
    private DailyStudy dailyStudy;

    public StudyKeyword(String keywords, String description, DailyStudy dailyStudy) {
        this.keywords = keywords;
        this.description = description;
        this.dailyStudy = dailyStudy;
    }
}
