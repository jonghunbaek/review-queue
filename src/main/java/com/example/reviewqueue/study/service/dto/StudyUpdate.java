package com.example.reviewqueue.study.service.dto;

import com.example.reviewqueue.study.domain.Study;
import com.example.reviewqueue.study.domain.StudyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudyUpdate {

    @NotNull(message = "학습 유형은 필수 값입니다.")
    private StudyType studyType;

    @NotBlank(message = "학습 제목은 필수 값입니다.")
    private String title;

    @NotBlank(message = "학습 내용은 필수 값입니다.")
    private String description;

    public void updateStudy(Study study) {
        study.update(this.studyType, this.title, this.description);
    }
}
