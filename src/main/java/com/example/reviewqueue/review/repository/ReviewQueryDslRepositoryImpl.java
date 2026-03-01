package com.example.reviewqueue.review.repository;

import com.example.reviewqueue.common.util.QuerydslResolver;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.dto.ReviewHistoryQueryCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static com.example.reviewqueue.review.domain.QReview.review;

@RequiredArgsConstructor
public class ReviewQueryDslRepositoryImpl implements ReviewQueryDslRepository {

    private final JPAQueryFactory query;
    private final QuerydslResolver resolver;

    @Override
    public Page<Review> findAllByHistory(ReviewHistoryQueryCondition condition, Pageable pageable) {
        JPAQuery<Review> contentQuery = query.selectFrom(review)
                .where(
                        review.member.id.eq(condition.getMemberId()),
                        review.reviewDate.between(condition.getStartDate(), condition.getEndDate()),
                        isCompletedEq(condition.getIsCompleted())
                )
                .orderBy(resolver.resolveOrders(pageable.getSort(), Review.class))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        JPAQuery<Long> countQuery = query.select(review.count())
                .from(review)
                .where(
                        review.member.id.eq(condition.getMemberId()),
                        review.reviewDate.between(condition.getStartDate(), condition.getEndDate()),
                        isCompletedEq(condition.getIsCompleted())
                );

        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    private BooleanExpression isCompletedEq(Boolean isCompleted) {
        if (isCompleted == null) return null;
        return isCompleted ? review.isCompleted.isTrue() : review.isCompleted.isFalse();
    }
}
