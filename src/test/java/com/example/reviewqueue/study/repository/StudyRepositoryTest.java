package com.example.reviewqueue.study.repository;

import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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

        Study study1 = new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member);
        Study study2 = new Study(StudyType.LECTURE, "김영한 자바 고급", "자바 고급 강의", member);
        Study study3 = new Study(StudyType.CODING_TEST, "프로그래머스 입국 순서", "이분 탐색 알고리즘", member);

        studyRepository.saveAll(List.of(study1, study2, study3));

        // when
        List<Study> studies = studyRepository.findAllByMemberId(member.getId());

        // then
        assertThat(studies).hasSize(3)
                .extracting("studyType", "title")
                .containsExactlyInAnyOrder(
                        tuple(StudyType.BOOK, "Real MySQL"),
                        tuple(StudyType.LECTURE, "김영한 자바 고급"),
                        tuple(StudyType.CODING_TEST, "프로그래머스 입국 순서")
                );
    }
}