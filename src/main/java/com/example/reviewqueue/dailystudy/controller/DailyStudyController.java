package com.example.reviewqueue.dailystudy.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.dailystudy.service.DailyStudyService;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyDetailInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyGeneralInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
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
    public DailyStudyGeneralInfo postDailyStudy(@RequestBody DailyStudySave dailyStudySave) {
        return dailyStudyService.save(dailyStudySave);
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

    // TODO :: 정렬 순서(학습 날짜), 기간별 조회 기능 보완 필요
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
