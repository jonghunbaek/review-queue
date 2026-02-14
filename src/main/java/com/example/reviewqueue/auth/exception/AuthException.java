package com.example.reviewqueue.auth.exception;

import com.example.reviewqueue.common.exception.GlobalException;
import com.example.reviewqueue.common.response.ResponseCode;

public class AuthException extends GlobalException {

    public AuthException(ResponseCode responseCode) {
        super(responseCode);
    }

    public AuthException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
