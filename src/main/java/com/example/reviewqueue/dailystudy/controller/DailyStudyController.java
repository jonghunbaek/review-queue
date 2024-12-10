package com.example.reviewqueue.dailystudy.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.dailystudy.service.DailyStudyService;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyDetailInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyGeneralInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
import com.example.reviewqueue.dailystudy.service.dto.StudyKeywordSave;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/daily-studies")
@RestController
public class DailyStudyController {

    private final DailyStudyService dailyStudyService;

    // TODO :: 일일 학습 저장과 학습 키워드 저장 API 분리하기.
    /**
     *  일일 학습 등록
     */
    @PostMapping
    public DailyStudyGeneralInfo postDailyStudy(@RequestBody DailyStudySave dailyStudySave, @RequestBody List<StudyKeywordSave> studyKeywordsSave) {
        return dailyStudyService.save(dailyStudySave, studyKeywordsSave);
    }

    /**
     *  일일 학습 단일 조회
     */
    @GetMapping("/{dailyStudyId}")
    public DailyStudyDetailInfo getDailyStudy(
            @PathVariable("dailyStudyId") Long dailyStudyId,
            @AuthenticatedMember Long memberId) {

        return dailyStudyService.findDailyStudyById(dailyStudyId, memberId);
    }

    /**
     *  일일 학습 전체 조회 - 학습 아이디 기반
     */
    @GetMapping
    public List<DailyStudyGeneralInfo> getDailyStudies(
            @RequestParam(required = false) Long studyId,
            @AuthenticatedMember Long memberId) {

        return dailyStudyService.findAllByStudyId(studyId, memberId);
    }
}
