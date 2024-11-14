package com.example.reviewqueue.reminder.repository;

import com.example.reviewqueue.reminder.domain.ReviewReminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReminderRepository extends JpaRepository<ReviewReminder, Long> {
}
