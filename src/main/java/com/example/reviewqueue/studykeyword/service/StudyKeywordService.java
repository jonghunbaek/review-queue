package com.example.reviewqueue.studykeyword.service;

import com.example.reviewqueue.common.response.ResponseCode;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.exception.DailyStudyException;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.dailystudy.service.dto.StudyKeywordSave;
import com.example.reviewqueue.studykeyword.domain.StudyKeyword;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.reviewqueue.common.util.GlobalValidator.validateAccessPermission;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyKeywordService {

    private final DailyStudyRepository dailyStudyRepository;
    private final StudyKeywordRepository studyKeywordRepository;

    public void save(List<StudyKeywordSave> studyKeywordsSave, Long dailyStudyId, Long memberId) {
        DailyStudy dailyStudy = dailyStudyRepository.findById(dailyStudyId)
                .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailyStudyId, ResponseCode.E12000));

        validateAccessPermission(memberId, dailyStudy.getStudy().getMember().getId());

        List<StudyKeyword> studyKeywords = studyKeywordsSave.stream()
                .map(dto -> dto.toEntity(dailyStudy))
                .toList();

        studyKeywordRepository.saveAll(studyKeywords);
    }
}
