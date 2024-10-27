package com.example.reviewqueue.dailystudy.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.study.domain.Study;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class DailyStudy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime studyDateTime;

    @ManyToOne
    private Study study;

    public DailyStudy(LocalDateTime studyDateTime, Study study) {
        this.studyDateTime = studyDateTime;
        this.study = study;
    }
}
