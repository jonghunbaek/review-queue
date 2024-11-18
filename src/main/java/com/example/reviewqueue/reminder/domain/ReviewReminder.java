package com.example.reviewqueue.reminder.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewReminder extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reminderDate;

    private boolean isRead;

    @ManyToOne
    private Member member;

    public ReviewReminder(LocalDate reminderDate, Member member) {
        this.reminderDate = reminderDate;
        this.member = member;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
