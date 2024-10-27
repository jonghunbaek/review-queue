package com.example.reviewqueue.dailystudy.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class StudyContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keywords;

    private String description;

    @ManyToOne
    private DailyStudy dailyStudy;

    public StudyContent(String keywords, String description, DailyStudy dailyStudy) {
        this.keywords = keywords;
        this.description = description;
        this.dailyStudy = dailyStudy;
    }
}
