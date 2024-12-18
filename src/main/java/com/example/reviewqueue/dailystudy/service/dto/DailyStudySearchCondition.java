package com.example.reviewqueue.dailystudy.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyStudySearchCondition {

    public static final int DEFAULT_SEARCH_PERIOD = 30;

    @NotNull(message = "학습 아이디는 필수 값입니다.")
    private Long studyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean createDateSortDesc;

    public DailyStudySearchCondition(Long studyId, LocalDate startDate, LocalDate endDate, Boolean createDateSortDesc) {
        initPeriod(startDate, endDate);

        this.studyId = studyId;
        this.startDate = startDate;
        this.endDate = endDate;

        if (createDateSortDesc == null) {
            this.createDateSortDesc = false;
        } else {
            this.createDateSortDesc = createDateSortDesc;
        }
    }

    private void initPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (endDate == null) {
            endDate = today;
        }

        if (startDate == null) {
            startDate = today.minusDays(DEFAULT_SEARCH_PERIOD);
        }

        validateEndDateEarlierThanStartDate(startDate, endDate);
    }

    private void validateEndDateEarlierThanStartDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be before start date");
        }
    }
}
