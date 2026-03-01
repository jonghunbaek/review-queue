package com.example.reviewqueue.common.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Sort;

public class QuerydslResolver {

    public <T> OrderSpecifier<?>[] resolveOrders(Sort sort, Class<T> classType) {
        PathBuilder<T> pathBuilder = new PathBuilder<>(classType, toVariableStyle(classType));

        return sort.stream()
                .map(order -> new OrderSpecifier(convertQuerydslOrder(order), pathBuilder.get(order.getProperty())))
                .toArray(OrderSpecifier[]::new);
    }

    private static <T> String toVariableStyle(Class<T> classType) {
        String simpleName = classType.getSimpleName();

        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    private Order convertQuerydslOrder(Sort.Order order) {
        return order.isAscending() ? Order.ASC : Order.DESC;
    }

    public <T> StringPath resolveStringPath(String targetField, Class<T> classType) {
        PathBuilder<T> pathBuilder = new PathBuilder<>(classType, classType.getSimpleName().toLowerCase());

        return pathBuilder.getString(targetField);
    }
}
