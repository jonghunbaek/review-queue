package com.example.reviewqueue.studykeyword.service;

import com.example.reviewqueue.common.exception.AccessDeniedException;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.studykeyword.exception.StudyKeywordException;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class StudyKeywordServiceTest {

    @Autowired
    private StudyKeywordService studyKeywordService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyKeywordRepository studyKeywordRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("학습 키워드를 비활성화한다.")
    @Test
    void inactivate() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위1", LocalDateTime.of(2024, 12, 15, 0, 0, 0), study));
        StudyKeyword studyKeyword = studyKeywordRepository.save(new StudyKeyword("인덱스", "B-Tree 인덱스 설명", dailyStudy));

        // when
        studyKeywordService.inactivate(studyKeyword.getId(), member.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        assertThat(studyKeywordRepository.findById(studyKeyword.getId())).isEmpty();
    }

    @DisplayName("존재하지 않는 학습 키워드를 비활성화하면 예외가 발생한다.")
    @Test
    void inactivate_studyKeywordNotFound() {
        // given
        Member member = memberRepository.save(new Member("test2@email.com", "password", "테스터2"));
        Long nonExistentStudyKeywordId = 999L;

        // when & then
        assertThatThrownBy(() -> studyKeywordService.inactivate(nonExistentStudyKeywordId, member.getId()))
                .isInstanceOf(StudyKeywordException.class);
    }

    @DisplayName("다른 회원의 학습 키워드를 비활성화하면 접근 권한 예외가 발생한다.")
    @Test
    void inactivate_accessDenied() {
        // given
        Member owner = memberRepository.save(new Member("owner@email.com", "password", "소유자"));
        Member other = memberRepository.save(new Member("other@email.com", "password", "다른회원"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", owner));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("범위1", LocalDateTime.of(2024, 12, 15, 0, 0, 0), study));
        StudyKeyword studyKeyword = studyKeywordRepository.save(new StudyKeyword("인덱스", "B-Tree 인덱스 설명", dailyStudy));

        // when & then
        assertThatThrownBy(() -> studyKeywordService.inactivate(studyKeyword.getId(), other.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }
}
