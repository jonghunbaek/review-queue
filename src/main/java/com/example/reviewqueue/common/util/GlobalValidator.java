package com.example.reviewqueue.common.util;

import com.example.reviewqueue.common.exception.AccessDeniedException;

import static com.example.reviewqueue.common.response.ResponseCode.E00007;

public class GlobalValidator {

    public static void validateAccessPermission(long idFromToken, long idFromEntity) {
        if (idFromToken != idFromEntity) {
            throw new AccessDeniedException(E00007);
        }
    }
}
