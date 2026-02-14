package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.common.config.querydsl.QuerydslConfig;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class StudyKeywordRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private StudyKeywordRepository studyKeywordRepository;

    @DisplayName("저장된 학습 키워드를 일일 학습 아이디로 조회한다.")
    @Test
    void findAllByDailyStudyId() {
        // given
        Member member = memberRepository.save(new Member("test@email.com", "password", "테스터"));
        Study study = studyRepository.save(new Study(StudyType.BOOK, "Real MySQL", "MySQL 관련 도서", member));
        DailyStudy dailyStudy = dailyStudyRepository.save(new DailyStudy("8.인덱스", LocalDateTime.now(), study));

        StudyKeyword keyword1 = new StudyKeyword("B-Tree 인덱스", "조회 속도를 높이기 위한 인덱스", dailyStudy);
        StudyKeyword keyword2 = new StudyKeyword("R-Tree 인덱스", "공간 정보를 저장하기 위한 인덱스", dailyStudy);
        studyKeywordRepository.saveAll(List.of(keyword1, keyword2));

        // when
        List<StudyKeyword> keywords = studyKeywordRepository.findAllByDailyStudyId(dailyStudy.getId());

        // then
        assertThat(keywords).hasSize(2)
                .extracting("keyword", "description")
                .containsExactlyInAnyOrder(
                        tuple("B-Tree 인덱스", "조회 속도를 높이기 위한 인덱스"),
                        tuple("R-Tree 인덱스", "공간 정보를 저장하기 위한 인덱스")
                );
    }
}
