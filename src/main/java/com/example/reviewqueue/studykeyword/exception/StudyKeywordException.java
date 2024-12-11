package com.example.reviewqueue.studykeyword.exception;

import com.example.reviewqueue.common.exception.GlobalException;
import com.example.reviewqueue.common.response.ResponseCode;

public class StudyKeywordException extends GlobalException {

    public StudyKeywordException(ResponseCode responseCode) {
        super(responseCode);
    }

    public StudyKeywordException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
