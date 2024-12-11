package com.example.reviewqueue.dailystudy.service;

import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyGeneralInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"/member.sql", "/study.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
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
        Member member = memberRepository.findAll().get(0);
        Study study = studyRepository.findAll().get(0);
        DailyStudySave dailyStudySave = new DailyStudySave(study.getId(), "8.인덱스(p230 ~ p250");

        // when
        DailyStudyGeneralInfo savingInfo = dailyStudyService.save(dailyStudySave);
        List<StudyKeyword> keywords = studyKeywordRepository.findAllByDailyStudyId(savingInfo.getDailyStudyId());

        // then
        assertThat(savingInfo.getStudyRange()).isEqualTo("8.인덱스(p230 ~ p250");
    }
}