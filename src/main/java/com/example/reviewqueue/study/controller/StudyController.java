package com.example.reviewqueue.study.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.study.controller.dto.StudySaveInfo;
import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.service.StudyService;
import com.example.reviewqueue.study.service.dto.StudyInfo;
import com.example.reviewqueue.study.service.dto.StudyUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/studies")
@RestController
public class StudyController implements StudyApi {

    private final StudyService studyService;

    /**
     *  학습 등록
     */
    @PostMapping
    public void saveStudy(@RequestBody StudySaveInfo studySaveInfo, @AuthenticatedMember Long memberId) {
        studyService.save(studySaveInfo.toStudySave(), memberId);
    }

    /**
     *  학습 단일 조회
     */
    @GetMapping("/{studyId}")
    public StudyInfo getStudyById(
            @PathVariable("studyId") Long studyId,
            @AuthenticatedMember Long memberId) {

        return studyService.findStudyInfoBy(studyId, memberId);
    }

    /**
     *  학습 전체 조회
     */
    @GetMapping
    public List<StudyInfo> getAllStudiesById(@RequestParam(required = false) String studyType, @AuthenticatedMember Long memberId) {
        if (studyType != null && !studyType.isBlank()) {
            return studyService.findAllStudyInfosBy(StudyType.valueOf(studyType), memberId);
        }

        return studyService.findAllStudyInfosBy(memberId);
    }

    /**
     *  학습 수정
     */
    @PutMapping("/{studyId}")
    public void putStudy(@RequestBody @Valid StudyUpdate studyUpdate, @PathVariable("studyId") Long studyId, @AuthenticatedMember Long memberId) {
        studyService.update(studyUpdate, studyId, memberId);
    }
}
