package com.example.reviewqueue.studykeyword.controller;

import com.example.reviewqueue.common.resolver.AuthenticatedMember;
import com.example.reviewqueue.dailystudy.service.dto.StudyKeywordSave;
import com.example.reviewqueue.studykeyword.service.StudyKeywordService;
import com.example.reviewqueue.studykeyword.service.dto.StudyKeywordUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study-keywords")
@RestController
public class StudyKeywordController implements StudyKeywordApi {

    private final StudyKeywordService studyKeywordService;

    @PostMapping("/{dailyStudyId}")
    public void postStudyKeyword(@RequestBody List<StudyKeywordSave> studyKeywordsSave, @PathVariable Long dailyStudyId, @AuthenticatedMember Long memberId) {
        studyKeywordService.save(studyKeywordsSave, dailyStudyId, memberId);
    }

    @PatchMapping("/{studyKeywordId}")
    public void patchStudyKeyword(@RequestBody @Valid StudyKeywordUpdate studyKeywordUpdate, @PathVariable Long studyKeywordId, @AuthenticatedMember Long memberId) {
        studyKeywordService.updateKeyword(studyKeywordUpdate, studyKeywordId, memberId);
    }

    @DeleteMapping("/{studyKeywordId}")
    public void deleteStudyKeyword(@PathVariable Long studyKeywordId, @AuthenticatedMember Long memberId) {
        studyKeywordService.inactivate(studyKeywordId, memberId);
    }
}
