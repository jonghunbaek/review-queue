package com.example.reviewqueue.common.exception;

import com.example.reviewqueue.common.response.ResponseCode;

public class AccessDeniedException extends GlobalException {

    public AccessDeniedException(ResponseCode responseCode) {
        super(responseCode);
    }

    public AccessDeniedException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
