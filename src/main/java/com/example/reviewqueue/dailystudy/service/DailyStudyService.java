package com.example.reviewqueue.dailystudy.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.dailystudy.exception.DailyStudyException;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import com.example.reviewqueue.dailystudy.service.dto.*;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.exception.StudyException;
import com.example.reviewqueue.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.E11000;
import static com.example.reviewqueue.common.response.ResponseCode.E12000;
import static com.example.reviewqueue.common.util.GlobalValidator.*;

@RequiredArgsConstructor
@Transactional
@Service
public class DailyStudyService {

    private final StudyRepository studyRepository;
    private final DailyStudyRepository dailyStudyRepository;
    private final StudyKeywordRepository studyKeywordRepository;

    public DailyStudyGeneralInfo save(DailyStudySave dailyStudySave) {
        Study study = findStudyById(dailyStudySave.getStudyId());
        DailyStudy dailyStudy = dailyStudyRepository.save(dailyStudySave.toEntity(study));

        return DailyStudyGeneralInfo.of(dailyStudy);
    }

    private Study findStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException("studyId :: " + studyId, E11000));
    }

    public DailyStudyDetailInfo findDailyStudyById(Long dailyStudyId, Long memberId) {
        DailyStudy dailyStudy = dailyStudyRepository.findById(dailyStudyId)
                .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailyStudyId, E12000));

        validateAccessPermission(memberId, dailyStudy.getStudy().getMember().getId());

        List<StudyKeyword> studyKeywords = studyKeywordRepository.findAllByDailyStudyId(dailyStudyId);
        List<StudyKeywordInfo> studyKeywordsInfo = studyKeywords.stream()
                .map(StudyKeywordInfo::of)
                .toList();

        return new DailyStudyDetailInfo(DailyStudyGeneralInfo.of(dailyStudy), studyKeywordsInfo);
    }

    public List<DailyStudyGeneralInfo> findAllByStudyId(Long studyId, Long memberId) {
        List<DailyStudy> dailyStudies = dailyStudyRepository.findAllByStudyId(studyId);

        validateAccessPermission(memberId, dailyStudies.get(0).getStudy().getMember().getId());

        return dailyStudies.stream()
                .map(DailyStudyGeneralInfo::of)
                .toList();
    }
}
