package com.example.reviewqueue.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseForm<T> {

    private String code;
    private String message;
    private T body;

    @Builder
    private ResponseForm(String code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public static <T> ResponseForm<T> of(ResponseCode code) {
        return ResponseForm.<T>builder()
                .code(code.name())
                .message(code.getMessage())
                .build();
    }

    public static <T> ResponseForm<T> from(ResponseCode code, T body) {
        return ResponseForm.<T>builder()
                .code(code.name())
                .message(code.getMessage())
                .body(body)
                .build();
    }
}
