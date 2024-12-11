package com.example.reviewqueue.studykeyword.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudyKeywordUpdate {

    @NotNull
    private String keyword;

    @NotNull
    private String description;
}
