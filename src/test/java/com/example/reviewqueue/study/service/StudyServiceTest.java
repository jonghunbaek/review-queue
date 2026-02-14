package com.example.reviewqueue.study.service;

import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.service.dto.StudyInfo;
import com.example.reviewqueue.study.service.dto.StudySave;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class StudyServiceTest {

    @Autowired
    private StudyService studyService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("학습 정보를 저장하고 조회한다.")
    @Test
    void saveAndFindById() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        StudySave studySave = new StudySave(StudyType.BOOK, "소프트웨어 장인", "개발자로서의 태도");

        // when
        long studyId = studyService.save(studySave, member.getId());
        StudyInfo studyInfo = studyService.findStudyInfoBy(studyId, member.getId());

        // then
        assertAll(
                () -> assertEquals(StudyType.BOOK, studyInfo.getStudyType()),
                () -> assertEquals("소프트웨어 장인", studyInfo.getTitle()),
                () -> assertEquals("개발자로서의 태도", studyInfo.getDescription())
        );
    }

    @DisplayName("회원 아이디로 등록된 모든 학습을 조회한다.")
    @Test
    void findAllByMemberId() {
        // given
        Member member = memberRepository.save(new Member("test2@email.com", "password", "테스터2"));
        StudySave studySave1 = new StudySave(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서");
        StudySave studySave2 = new StudySave(StudyType.LECTURE, "김영한 자바 고급", "자바 고급 강의");
        StudySave studySave3 = new StudySave(StudyType.CODING_TEST, "프로그래머스 입국 순서", "이분 탐색 알고리즘");

        long studyId1 = studyService.save(studySave1, member.getId());
        long studyId2 = studyService.save(studySave2, member.getId());
        long studyId3 = studyService.save(studySave3, member.getId());

        // when
        List<StudyInfo> studyInfos = studyService.findAllStudyInfosBy(member.getId());

        // then
        assertThat(studyInfos).hasSize(3)
                .extracting("studyId", "studyType", "title")
                .containsExactlyInAnyOrder(
                        tuple(studyId1, StudyType.BOOK, "Real MySQL"),
                        tuple(studyId2, StudyType.LECTURE, "김영한 자바 고급"),
                        tuple(studyId3, StudyType.CODING_TEST, "프로그래머스 입국 순서")
                );
    }
}
