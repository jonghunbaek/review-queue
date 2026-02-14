package com.example.reviewqueue.dailystudy.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyGeneralInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySearchCondition;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class DailyStudyServiceTest {

    @Autowired
    private DailyStudyService dailyStudyService;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyKeywordRepository studyKeywordRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("일일 학습과 학습 키워드를 저장한다.")
    @Test
    void saveDailyStudyAndKeywords() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudySave dailyStudySave = new DailyStudySave(study.getId(), "8.인덱스(p230 ~ p250");

        // when
        DailyStudyGeneralInfo savingInfo = dailyStudyService.save(dailyStudySave);

        // then
        assertThat(savingInfo.getStudyRange()).isEqualTo("8.인덱스(p230 ~ p250");
    }

    @DisplayName("조건에 일치하는 일일 학습을 조회한다.")
    @Test
    void findAllByConditions() {
        // given
        Member member = memberRepository.save(new Member("test2@email.com", "password", "테스터2"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy1 = new DailyStudy("범위1", LocalDateTime.of(2024, 12, 15, 0, 0, 0), study);
        DailyStudy dailyStudy2 = new DailyStudy("범위2", LocalDateTime.of(2024, 12, 16, 0, 0, 0), study);
        DailyStudy dailyStudy3 = new DailyStudy("범위3", LocalDateTime.of(2024, 12, 17, 0, 0, 0), study);
        DailyStudy dailyStudy4 = new DailyStudy("범위4", LocalDateTime.of(2024, 12, 18, 0, 0, 0), study);
        DailyStudy dailyStudy5 = new DailyStudy("범위5", LocalDateTime.of(2024, 12, 19, 0, 0, 0), study);

        dailyStudyRepository.saveAll(List.of(dailyStudy1, dailyStudy2, dailyStudy3, dailyStudy4, dailyStudy5));

        DailyStudySearchCondition conditions = new DailyStudySearchCondition(
                study.getId(),
                LocalDate.of(2024, 12, 15),
                LocalDate.of(2024, 12, 17),
                false);

        // when
        List<DailyStudyGeneralInfo> dailyStudies = dailyStudyService.findAllByConditions(conditions, member.getId());

        // then
        assertThat(dailyStudies).hasSize(3)
                .extracting("studyRange")
                .containsExactlyInAnyOrder("범위1", "범위2", "범위3");
    }
}
