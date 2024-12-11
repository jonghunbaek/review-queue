package com.example.reviewqueue.dailystudy.service;

import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyGeneralInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
import com.example.reviewqueue.dailystudy.service.dto.StudyKeywordSave;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.repository.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        StudyKeywordSave keyword1 = new StudyKeywordSave("B-Tree 인덱스", "조회 성능을 높이기 위한 인덱스");
        StudyKeywordSave keyword2 = new StudyKeywordSave("R-Tree 인덱스", "공간 정보를 다루기 위한 인덱스");

        // when
        DailyStudyGeneralInfo savingInfo = dailyStudyService.save(dailyStudySave, List.of(keyword1, keyword2));
        List<StudyKeyword> keywords = studyKeywordRepository.findAllByDailyStudyId(savingInfo.getDailyStudyId());

        // then
        assertAll(
                () -> assertEquals(savingInfo.getDailyStudyId(), 1),
                () -> assertEquals(keywords.size(), 2)
        );
    }
}