package com.example.reviewqueue.reminder.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.reminder.domain.ReviewReminder;
import com.example.reviewqueue.reminder.exception.ReviewReminderException;
import com.example.reviewqueue.reminder.repository.ReviewReminderRepository;
import com.example.reviewqueue.reminder.service.dto.ReminderInfo;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewReminderServiceTest {

    @Autowired
    private ReviewReminderService reviewReminderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewReminderRepository reviewReminderRepository;

    @DisplayName("사용자 아이디에 해당하는 읽지 않은 알람을 모두 조회한다.")
    @Test
    void findUnreadReminderReviewData() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        LocalDate studyDate = LocalDate.of(2024, 10, 31);
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", studyDate.atTime(0,0), study));

        LocalDate reviewDate1 = LocalDate.of(2024, 11, 11);
        LocalDate reviewDate2 = LocalDate.of(2024, 11, 15);
        Review review1 = new Review(reviewDate1.minusDays(1L), reviewDate1, dailyStudy);
        Review review2 = new Review(reviewDate2.minusDays(1L), reviewDate2, dailyStudy);
        reviewRepository.saveAll(List.of(review1, review2));

        ReviewReminder reviewReminder1 = new ReviewReminder(reviewDate1, member, review1);
        ReviewReminder reviewReminder2 = new ReviewReminder(reviewDate2, member, review2);
        reviewReminderRepository.saveAll(List.of(reviewReminder1, reviewReminder2));

        // when
        List<ReminderInfo> reminders = reviewReminderService.findUnreadReminderReviewData(member.getId());

        // then
        assertThat(reminders).hasSize(2)
                .extracting("reminderDate")
                .containsExactlyInAnyOrder(reviewDate1, reviewDate2);
    }

    @DisplayName("미읽은 알림 조회 시 각 알림에 연결된 Review의 studyTitle과 dailyStudyRange가 포함된다.")
    @Test
    void findUnreadReminderReviewData_containsReviewInfo() {
        // given
        Member member = memberRepository.save(new Member("test2@email.com", "password", "테스터2"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스, p200-210", LocalDate.of(2024, 10, 31).atTime(0, 0), study));

        LocalDate reviewDate = LocalDate.of(2024, 11, 11);
        Review review = reviewRepository.save(new Review(reviewDate.minusDays(1L), reviewDate, dailyStudy));

        ReviewReminder reminder = new ReviewReminder(reviewDate, member, review);
        reviewReminderRepository.save(reminder);

        // when
        List<ReminderInfo> reminders = reviewReminderService.findUnreadReminderReviewData(member.getId());

        // then
        assertThat(reminders).hasSize(1);
        ReminderInfo reminderInfo = reminders.get(0);
        assertThat(reminderInfo.getReviewId()).isEqualTo(review.getId());
        assertThat(reminderInfo.getStudyTitle()).isEqualTo("Real MySQL");
        assertThat(reminderInfo.getDailyStudyRange()).isEqualTo("8장 인덱스, p200-210");
    }

    @DisplayName("미읽은 알림이 없으면 빈 리스트를 반환한다.")
    @Test
    void findUnreadReminderReviewData_empty() {
        // given
        Member member = memberRepository.save(new Member("test3@email.com", "password", "테스터3"));

        // when
        List<ReminderInfo> reminders = reviewReminderService.findUnreadReminderReviewData(member.getId());

        // then
        assertThat(reminders).isEmpty();
    }

    @DisplayName("이미 읽은 알림은 미읽은 알림 조회에 포함되지 않는다.")
    @Test
    void findUnreadReminderReviewData_excludesRead() {
        // given
        Member member = memberRepository.save(new Member("test4@email.com", "password", "테스터4"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스", LocalDate.of(2024, 10, 31).atTime(0, 0), study));

        LocalDate reviewDate1 = LocalDate.of(2024, 11, 11);
        LocalDate reviewDate2 = LocalDate.of(2024, 11, 15);
        Review review1 = reviewRepository.save(new Review(reviewDate1.minusDays(1L), reviewDate1, dailyStudy));
        Review review2 = reviewRepository.save(new Review(reviewDate2.minusDays(1L), reviewDate2, dailyStudy));

        ReviewReminder readReminder = new ReviewReminder(reviewDate1, member, review1);
        readReminder.read();
        ReviewReminder unreadReminder = new ReviewReminder(reviewDate2, member, review2);
        reviewReminderRepository.saveAll(List.of(readReminder, unreadReminder));

        // when
        List<ReminderInfo> reminders = reviewReminderService.findUnreadReminderReviewData(member.getId());

        // then
        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getReminderDate()).isEqualTo(reviewDate2);
    }

    @DisplayName("알림 확인 처리 시 isRead가 true로 변경된다.")
    @Test
    void read() {
        // given
        Member member = memberRepository.save(new Member("test5@email.com", "password", "테스터5"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8장 인덱스", LocalDate.of(2024, 10, 31).atTime(0, 0), study));

        LocalDate reviewDate = LocalDate.of(2024, 11, 11);
        Review review = reviewRepository.save(new Review(reviewDate.minusDays(1L), reviewDate, dailyStudy));
        ReviewReminder reminder = reviewReminderRepository.save(new ReviewReminder(reviewDate, member, review));

        // when
        reviewReminderService.read(reminder.getId(), member.getId());

        // then
        ReviewReminder found = reviewReminderRepository.findById(reminder.getId()).orElseThrow();
        assertThat(found.isRead()).isTrue();
    }

    @DisplayName("존재하지 않는 알림 확인 처리 시 예외가 발생한다.")
    @Test
    void read_notFound() {
        // given
        Member member = memberRepository.save(new Member("test6@email.com", "password", "테스터6"));

        // when & then
        assertThatThrownBy(() -> reviewReminderService.read(999L, member.getId()))
                .isInstanceOf(ReviewReminderException.class);
    }
}
