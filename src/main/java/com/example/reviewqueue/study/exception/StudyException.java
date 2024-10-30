package com.example.reviewqueue.study.exception;

import com.example.reviewqueue.common.exception.GlobalException;
import com.example.reviewqueue.common.response.ResponseCode;

public class StudyException extends GlobalException {

    public StudyException(ResponseCode responseCode) {
        super(responseCode);
    }

    public StudyException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
