package com.example.reviewqueue.common.util;

public class GlobalValidator {

    public static void validateMemberId(long idFromToken, long idFromEntity) {
        if (idFromToken != idFromEntity) {
            throw new IllegalStateException("해당 엔티티에 대한 접근 권한이 없습니다.");
        }
    }
}
