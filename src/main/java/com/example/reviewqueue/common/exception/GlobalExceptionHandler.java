package com.example.reviewqueue.common.exception;

import com.example.reviewqueue.common.response.ResponseForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.reviewqueue.common.response.ResponseCode.E99999;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> handleGlobalException(GlobalException e) {
        log.error("e :: ", e);

        ResponseForm<Object> response = ResponseForm.of(e.getResponseCode());

        return ResponseEntity.status(e.getResponseCode().getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception e) {
        log.error("unexpected exception :: ", e);

        ResponseForm<Object> response = ResponseForm.of(E99999);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
