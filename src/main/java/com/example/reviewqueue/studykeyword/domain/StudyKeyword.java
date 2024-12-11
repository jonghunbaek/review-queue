package com.example.reviewqueue.studykeyword.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
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
        dailyStudy.getKeywords().add(this);
    }
}
