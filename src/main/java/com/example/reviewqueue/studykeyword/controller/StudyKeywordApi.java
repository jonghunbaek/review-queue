package com.example.reviewqueue.studykeyword.controller;

import com.example.reviewqueue.dailystudy.service.dto.StudyKeywordSave;
import com.example.reviewqueue.studykeyword.service.dto.StudyKeywordUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "학습 키워드", description = "학습 키워드 등록 및 수정")
public interface StudyKeywordApi {

    @Operation(summary = "학습 키워드 등록", security = @SecurityRequirement(name = "Bearer Authentication"))
    void postStudyKeyword(List<StudyKeywordSave> studyKeywordsSave, @Parameter(description = "일일 학습 ID") Long dailyStudyId, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "학습 키워드 수정", security = @SecurityRequirement(name = "Bearer Authentication"))
    void patchStudyKeyword(StudyKeywordUpdate studyKeywordUpdate, @Parameter(description = "학습 키워드 ID") Long studyKeywordId, @Parameter(hidden = true) Long memberId);

    @Operation(summary = "학습 키워드 비활성화", security = @SecurityRequirement(name = "Bearer Authentication"))
    void deleteStudyKeyword(@Parameter(description = "학습 키워드 ID") Long studyKeywordId, @Parameter(hidden = true) Long memberId);
}
