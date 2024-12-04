package com.example.reviewqueue.study.controller.dto;

import com.example.reviewqueue.study.domain.StudyType;
import com.example.reviewqueue.study.service.dto.StudySave;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudySaveInfo {

    @NotNull
    private StudyType studyType;

    @NotNull
    private String title;

    private String description;

    public StudySave toStudySave() {
        return new StudySave(studyType, title, description);
    }
}
