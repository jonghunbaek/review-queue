package com.example.reviewqueue.reminder.exception;

import com.example.reviewqueue.common.exception.GlobalException;
import com.example.reviewqueue.common.response.ResponseCode;

public class ReviewReminderException extends GlobalException {

    public ReviewReminderException(ResponseCode responseCode) {
        super(responseCode);
    }

    public ReviewReminderException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
