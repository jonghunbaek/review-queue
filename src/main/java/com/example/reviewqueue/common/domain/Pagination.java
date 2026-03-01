package com.example.reviewqueue.common.domain;

import lombok.Getter;

public class Pagination {

    @Getter
    private final int totalPages;

    @Getter
    private final long totalRows;

    private final boolean isFirst;

    private final boolean isLast;

    public Pagination(int totalPages, long totalRows, int currentPage) {
        this.totalPages = totalPages;
        this.totalRows = totalRows;
        this.isFirst = currentPage == 1;
        this.isLast = currentPage == totalPages;
    }

    public static Pagination fromMapper(long totalRows, int currentPage, int size) {
        int totalPages = (int) Math.ceil((double) totalRows/size);

        return new Pagination(totalPages, totalRows, currentPage);
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public boolean getIsLast() {
        return isLast;
    }
}
