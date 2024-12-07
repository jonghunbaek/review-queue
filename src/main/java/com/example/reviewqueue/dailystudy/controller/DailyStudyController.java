package com.example.reviewqueue.dailystudy.controller;

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

    /**
     *  일일 학습 등록
     */
    @PostMapping
    public DailyStudyGeneralInfo postDailyStudy(@RequestBody DailyStudySave dailyStudySave, List<StudyKeywordSave> studyKeywordsSave) {
        return dailyStudyService.save(dailyStudySave, studyKeywordsSave);
    }

    /**
     *  일일 학습 단일 조회
     */
    @GetMapping("{/dailyStudyId}")
    public DailyStudyDetailInfo getDailyStudy(@PathVariable Long dailyStudyId) {
        return dailyStudyService.findDailyStudyById(dailyStudyId);
    }

}