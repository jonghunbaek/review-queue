package com.example.reviewqueue.reminder.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_active = true")
@Entity
public class ReviewReminder extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reminderDate;

    private boolean isRead;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Review review;

    public ReviewReminder(LocalDate reminderDate, Member member, Review review) {
        this.reminderDate = reminderDate;
        this.member = member;
        this.review = review;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
