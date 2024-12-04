package com.example.reviewqueue.study.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.study.controller.dto.StudySaveInfo;
import com.example.reviewqueue.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/studies")
@RestController
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public void saveStudy(@RequestBody StudySaveInfo studySaveInfo, @AuthenticatedMember Long memberId) {
        studyService.save(studySaveInfo.toStudySave(), memberId);
    }
}
