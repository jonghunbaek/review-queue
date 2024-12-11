package com.example.reviewqueue.studykeyword.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study-keywords")
@RestController
public class StudyKeywordController {

    @PostMapping
    public void postStudyKeyword(@RequestParam Long dailyStudyId, @AuthenticatedMember Long memberId) {

    }

    @PatchMapping("/{studyKeywordId}")
    public void patchStudyKeyword(@PathVariable String studyKeywordId, @AuthenticatedMember Long memberId) {

    }
}
