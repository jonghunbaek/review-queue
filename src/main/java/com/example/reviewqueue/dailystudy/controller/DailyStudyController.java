package com.example.reviewqueue.dailystudy.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.dailystudy.service.DailyStudyService;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyDetailInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyGeneralInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySearchCondition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/daily-studies")
@RestController
public class DailyStudyController implements DailyStudyApi {

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

    /**
     *  일일 학습 전체 조회 - 학습 아이디 기반
     */
    @GetMapping
    public List<DailyStudyGeneralInfo> getDailyStudies(@Valid DailyStudySearchCondition searchCondition, @AuthenticatedMember Long memberId) {
        return dailyStudyService.findAllByConditions(searchCondition, memberId);
    }

    /**
     *  일일 학습 비활성화 (논리 삭제)
     */
    @DeleteMapping("/{dailyStudyId}")
    public void deleteDailyStudy(@PathVariable("dailyStudyId") Long dailyStudyId, @AuthenticatedMember Long memberId) {
        dailyStudyService.inactivate(dailyStudyId, memberId);
    }
}
