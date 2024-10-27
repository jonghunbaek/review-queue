package com.example.reviewqueue.review.domain;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class ReviewQueue {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reviewDateTime;

    @ManyToOne
    private DailyStudy dailyStudy;
}
