package com.example.reviewqueue.study.controller;

import com.example.reviewqueue.study.controller.dto.StudySaveInfo;
import com.example.reviewqueue.study.service.dto.StudyInfo;
import com.example.reviewqueue.study.service.dto.StudyUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "학습", description = "학습 CRUD")
public interface StudyApi {

    @Operation(summary = "학습 등록", security = @SecurityRequirement(name = "Bearer Authentication"))
    void saveStudy(StudySaveInfo studySaveInfo, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "학습 단일 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    StudyInfo getStudyById(@Parameter(description = "학습 ID") Long studyId, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "학습 전체 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    List<StudyInfo> getAllStudiesById(@Parameter(description = "학습 유형 (COURSE, BOOK, ALGORITHM)") String studyType, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "학습 수정", security = @SecurityRequirement(name = "Bearer Authentication"))
    void putStudy(StudyUpdate studyUpdate, @Parameter(description = "학습 ID") Long studyId, @Parameter(hidden = true) Long memberId);
}
