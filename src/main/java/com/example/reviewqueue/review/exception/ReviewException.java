package com.example.reviewqueue.review.exception;

import com.example.reviewqueue.common.exception.GlobalException;
import com.example.reviewqueue.common.response.ResponseCode;

public class ReviewException extends GlobalException {

    public ReviewException(ResponseCode responseCode) {
        super(responseCode);
    }

    public ReviewException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
