package com.example.reviewqueue.reviewqueue.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewQueue extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  이전 복습일
     */
    private LocalDate previousReviewDate;

    /**
     *  이번 복습일
     */
    private LocalDate reviewDate;

    /**
     *  복습 완료 여부
     */
    private boolean isCompleted;

    @ManyToOne
    private DailyStudy dailyStudy;

    @ManyToOne
    private Member member;

    public ReviewQueue(LocalDate previousReviewDate, LocalDate reviewDate, DailyStudy dailyStudy) {
        this.previousReviewDate = previousReviewDate;
        this.reviewDate = reviewDate;
        this.isCompleted = false;
        this.dailyStudy = dailyStudy;
        this.member = dailyStudy.getStudy().getMember();
    }
}
