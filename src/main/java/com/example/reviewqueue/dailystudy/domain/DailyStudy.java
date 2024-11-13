package com.example.reviewqueue.dailystudy.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.study.domain.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DailyStudy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  학습 범위
     */
    private String studyRange;

    /**
     *  학습 일시
     */
    private LocalDateTime studyDateTime;

    // TODO :: 연관 관계 편의 메서드 추가 및 설정 수정 필요
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dailyStudy")
    private List<StudyKeyword> keywords;

    @ManyToOne
    private Study study;

    public DailyStudy(String studyRange, LocalDateTime studyDateTime, Study study) {
        this.studyRange = studyRange;
        this.studyDateTime = studyDateTime;
        this.study = study;
    }
}
