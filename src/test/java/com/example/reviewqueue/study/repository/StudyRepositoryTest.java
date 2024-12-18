package com.example.reviewqueue.study.repository;

import com.example.reviewqueue.common.config.querydsl.QuerydslConfig;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.example.reviewqueue.study.domain.StudyType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Import(QuerydslConfig.class)
@Sql(scripts = {"/member.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ActiveProfiles("test")
@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 아이디로 등록된 모든 학습을 조회한다.")
    @Test
    void findAllByMemberId() {
        // given
        Member member = memberRepository.findAll().get(0);

        Study study1 = new Study(BOOK, "Real MySQL", "MySQL 관련 도서", member);
        Study study2 = new Study(LECTURE, "김영한 자바 고급", "자바 고급 강의", member);
        Study study3 = new Study(CODING_TEST, "프로그래머스 입국 순서", "이분 탐색 알고리즘", member);

        studyRepository.saveAll(List.of(study1, study2, study3));

        // when
        List<Study> studies = studyRepository.findAllByMemberId(member.getId());

        // then
        assertThat(studies).hasSize(3)
                .extracting("studyType", "title")
                .containsExactlyInAnyOrder(
                        tuple(BOOK, "Real MySQL"),
                        tuple(LECTURE, "김영한 자바 고급"),
                        tuple(CODING_TEST, "프로그래머스 입국 순서")
                );
    }

    @DisplayName("학습 유형과 사용자 아이디에 일치하는 모든 학습을 조회한다.")
    @Test
    void findAllStudyInfosBy() {
        // given
        Member member = memberRepository.findAll().get(0);
        Study study1 = new Study(BOOK, "Real MySQL", "MySQL 관련 도서", member);
        Study study2 = new Study(LECTURE, "김영한 자바 고급", "자바 고급 강의", member);
        Study study3 = new Study(LECTURE, "김영한 스프링 고급", "스프링 고급 강의", member);
        Study study4 = new Study(CODING_TEST, "프로그래머스 입국 순서", "이분 탐색 알고리즘", member);

        studyRepository.saveAll(List.of(study1, study2, study3));

        // when
        List<Study> studies = studyRepository.findAllByStudyTypeAndMemberId(LECTURE, member.getId());

        // then
        assertThat(studies).hasSize(2)
                .extracting("studyType", "title")
                .containsExactlyInAnyOrder(
                        tuple(LECTURE, "김영한 자바 고급"),
                        tuple(LECTURE, "김영한 스프링 고급")
                );
    }
}