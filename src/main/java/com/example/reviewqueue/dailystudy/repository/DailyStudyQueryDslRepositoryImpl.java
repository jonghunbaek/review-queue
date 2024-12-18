package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.reviewqueue.dailystudy.domain.QDailyStudy.dailyStudy;

@RequiredArgsConstructor
public class DailyStudyQueryDslRepositoryImpl implements DailyStudyQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public List<DailyStudy> findAllByCondition(Long studyId, LocalDate startDate, LocalDate endDate, boolean createDateSortDesc) {
        return query.selectFrom(dailyStudy)
                .where(dailyStudy.study.id.eq(studyId), dailyStudy.studyDateTime.between(startDate.atStartOfDay(), endDate.atStartOfDay()))
                .orderBy(orderDirection(createDateSortDesc))
                .fetch();
    }

    private OrderSpecifier<LocalDateTime> orderDirection(boolean createDateSortDesc) {
        return createDateSortDesc ? dailyStudy.createdDate.asc() : dailyStudy.createdDate.desc();
    }
}
