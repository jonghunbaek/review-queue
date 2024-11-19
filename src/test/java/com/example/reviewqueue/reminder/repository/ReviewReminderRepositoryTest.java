package com.example.reviewqueue.reminder.repository;

import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.reminder.domain.ReviewReminder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"/member.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ActiveProfiles("test")
@DataJpaTest
class ReviewReminderRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewReminderRepository reminderRepository;

    @DisplayName("읽지 않은 알림을 조회한다.")
    @Test
    void existsReviewRemindersByMemberIdAndReadIsFalse() {
        // given
        Member member = memberRepository.findAll().get(0);
        ReviewReminder reminder1 = new ReviewReminder(LocalDate.of(2024, 11, 20), member);
        ReviewReminder reminder2 = new ReviewReminder(LocalDate.of(2024, 11, 21), member);
        reminder1.read();

        reminderRepository.saveAll(List.of(reminder1, reminder2));

        // when
        List<ReviewReminder> reminders = reminderRepository.findAllByMemberIdAndIsReadIsFalse(member.getId());

        // then
        assertThat(reminders).hasSize(1);
    }
}