package com.example.reviewqueue.study.domain;

public enum StudyType {

    LECTURE("강의"),
    BOOK("도서"),
    CODING_TEST("코딩 테스트");

    private String description;

    StudyType(String description) {
        this.description = description;
    }
}
