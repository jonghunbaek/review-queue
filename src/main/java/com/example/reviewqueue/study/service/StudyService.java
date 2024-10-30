package com.example.reviewqueue.study.service;

import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.exception.MemberException;
import com.example.reviewqueue.member.repository.MemberRepository;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.exception.StudyException;
import com.example.reviewqueue.study.repository.StudyRepository;
import com.example.reviewqueue.study.service.dto.StudyInfo;
import com.example.reviewqueue.study.service.dto.StudySave;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.*;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    public void save(StudySave studySave, Long memberId) {
        Member member = findMemberById(memberId);

        studyRepository.save(studySave.toEntity(member));
    }

    public StudyInfo findStudyInfoBy(Long studyId) {
        Study study = findStudyById(studyId);

        return StudyInfo.of(study);
    }

    public List<StudyInfo> findAllStudyInfosBy(Long memberId) {
        List<Study> studies = studyRepository.findAllByMemberId(memberId);

        return studies.stream()
                .map(StudyInfo::of)
                .toList();
    }

    private Study findStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException("studyId :: " + studyId, E11000));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberException("memberId :: " + memberId, E10000));
    }
}
