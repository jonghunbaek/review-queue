package com.example.reviewqueue.study.service;

import com.example.reviewqueue.common.exception.AccessDeniedException;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.exception.StudyException;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.study.service.dto.StudyInfo;
import com.example.reviewqueue.study.service.dto.StudySave;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyKeywordRepository studyKeywordRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager entityManager;

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

    @DisplayName("학습을 비활성화하면 연관된 일일학습, 학습 키워드, 복습이 모두 비활성화된다.")
    @Test
    void inactivate() {
        // given
        Member member = memberRepository.save(new Member("test3@email.com", "password", "테스터3"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));

        DailyStudy dailyStudy1 = dailyStudyRepository.save(new DailyStudy("범위1", LocalDateTime.of(2024, 12, 15, 0, 0, 0), study));
        DailyStudy dailyStudy2 = dailyStudyRepository.save(new DailyStudy("범위2", LocalDateTime.of(2024, 12, 16, 0, 0, 0), study));

        studyKeywordRepository.save(new StudyKeyword("인덱스", "B-Tree 인덱스 설명", dailyStudy1));
        studyKeywordRepository.save(new StudyKeyword("트랜잭션", "ACID 설명", dailyStudy2));

        reviewRepository.save(new Review(LocalDate.of(2024, 12, 15), LocalDate.of(2024, 12, 16), dailyStudy1));
        reviewRepository.save(new Review(LocalDate.of(2024, 12, 16), LocalDate.of(2024, 12, 17), dailyStudy2));

        // when
        studyService.inactivate(study.getId(), member.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        assertAll(
                () -> assertThat(studyRepository.findById(study.getId())).isEmpty(),
                () -> assertThat(dailyStudyRepository.findAllByStudyId(study.getId())).isEmpty(),
                () -> assertThat(studyKeywordRepository.findAllByDailyStudyIdIn(List.of(dailyStudy1.getId(), dailyStudy2.getId()))).isEmpty(),
                () -> assertThat(reviewRepository.findAllByDailyStudyIdIn(List.of(dailyStudy1.getId(), dailyStudy2.getId()))).isEmpty()
        );
    }

    @DisplayName("존재하지 않는 학습을 비활성화하면 예외가 발생한다.")
    @Test
    void inactivate_studyNotFound() {
        // given
        Member member = memberRepository.save(new Member("test4@email.com", "password", "테스터4"));
        Long nonExistentStudyId = 999L;

        // when & then
        assertThatThrownBy(() -> studyService.inactivate(nonExistentStudyId, member.getId()))
                .isInstanceOf(StudyException.class);
    }

    @DisplayName("다른 회원의 학습을 비활성화하면 접근 권한 예외가 발생한다.")
    @Test
    void inactivate_accessDenied() {
        // given
        Member owner = memberRepository.save(new Member("owner@email.com", "password", "소유자"));
        Member other = memberRepository.save(new Member("other@email.com", "password", "다른회원"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", owner));

        // when & then
        assertThatThrownBy(() -> studyService.inactivate(study.getId(), other.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }
}
