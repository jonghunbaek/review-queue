package com.example.reviewqueue.dailystudy.controller;

import com.example.reviewqueue.dailystudy.service.dto.DailyStudyDetailInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudyGeneralInfo;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySave;
import com.example.reviewqueue.dailystudy.service.dto.DailyStudySearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "일일 학습", description = "일일 학습 등록 및 조회")
public interface DailyStudyApi {

    @Operation(summary = "일일 학습 등록")
    DailyStudyGeneralInfo postDailyStudy(DailyStudySave dailyStudySave);

    @Operation(summary = "일일 학습 단일 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    DailyStudyDetailInfo getDailyStudy(@Parameter(description = "일일 학습 ID") Long dailyStudyId, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "일일 학습 전체 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    List<DailyStudyGeneralInfo> getDailyStudies(DailyStudySearchCondition searchCondition, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "일일 학습 비활성화", description = "일일 학습 및 하위 학습키워드, 복습을 모두 비활성화합니다.", security = @SecurityRequirement(name = "Bearer Authentication"))
    void deleteDailyStudy(@Parameter(description = "일일 학습 ID") Long dailyStudyId, @Parameter(hidden = true) Long memberId);
}
