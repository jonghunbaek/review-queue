package com.example.reviewqueue.dailystudy.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.domain.StudyKeyword;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.repository.StudyKeywordRepository;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
import com.example.reviewqueue.dailystudy.service.dto.StudyKeywordSave;
import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.exception.StudyException;
import com.example.reviewqueue.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.E11000;

@RequiredArgsConstructor
@Transactional
@Service
public class DailyStudyService {

    private final StudyRepository studyRepository;
    private final DailyStudyRepository dailyStudyRepository;
    private final StudyKeywordRepository studyKeywordRepository;

    public DailyStudyInfo save(DailyStudySave dailyStudySave, List<StudyKeywordSave> studyKeywordsSave) {
        Study study = findStudyById(dailyStudySave.getStudyId());
        DailyStudy dailyStudy = dailyStudyRepository.save(dailyStudySave.toEntity(study));

        List<StudyKeyword> studyKeywords = studyKeywordsSave.stream()
                .map(dto -> dto.toEntity(dailyStudy))
                .toList();
        studyKeywordRepository.saveAll(studyKeywords);

        return DailyStudyInfo.of(dailyStudy);
    }

    private Study findStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException("studyId :: " + studyId, E11000));
    }
}
