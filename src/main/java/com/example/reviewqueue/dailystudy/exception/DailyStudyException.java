package com.example.reviewqueue.dailystudy.exception;

import com.example.reviewqueue.common.exception.GlobalException;
import com.example.reviewqueue.common.response.ResponseCode;

public class DailyStudyException extends GlobalException {

    public DailyStudyException(ResponseCode responseCode) {
        super(responseCode);
    }

    public DailyStudyException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
