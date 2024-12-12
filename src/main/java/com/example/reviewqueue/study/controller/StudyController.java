package com.example.reviewqueue.study.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.study.controller.dto.StudySaveInfo;
import com.example.reviewqueue.study.service.StudyService;
import com.example.reviewqueue.study.service.dto.StudyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/studies")
@RestController
public class StudyController {

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

    // TODO :: 정렬 순서, 학습 분류(studyType)별 조회 기능 보완 필요
    /**
     *  학습 전체 조회
     */
    @GetMapping
    public List<StudyInfo> getAllStudiesById(@AuthenticatedMember Long memberId) {
        return studyService.findAllStudyInfosBy(memberId);
    }
    
}
