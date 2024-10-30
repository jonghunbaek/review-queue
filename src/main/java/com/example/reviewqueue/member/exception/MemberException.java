package com.example.reviewqueue.member.exception;

import com.example.reviewqueue.common.exception.GlobalException;
import com.example.reviewqueue.common.response.ResponseCode;

public class MemberException extends GlobalException {

    public MemberException(ResponseCode responseCode) {
        super(responseCode);
    }

    public MemberException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
