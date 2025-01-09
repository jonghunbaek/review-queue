package com.example.reviewqueue.common.exception;

import com.example.reviewqueue.common.response.ResponseForm;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

import static com.example.reviewqueue.common.response.ResponseCode.E90001;
import static com.example.reviewqueue.common.response.ResponseCode.E99999;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String DEFAULT_BINDING_EXCEPTION_MESSAGE = "파라미터의 타입에 바인딩할 수 없는 값('%s')입니다.";
    public static final String DEFAULT_PARAMETER_MISSING_EXCEPTION_MESSAGE = "%s는 필수 값입니다.";

    /**
     *  Request body binding exception handler(요청 바디 값 바인딩 오류)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBindException(HttpMessageNotReadableException e) {
        log.error("Body parameter binding exception :: ", e);

        Map<String, String> errors = resolveErrorMessage(e.getCause());

        return ResponseEntity.badRequest()
                .body(createInputValidationResponse(errors));
    }

    private Map<String, String> resolveErrorMessage(Throwable cause) {
        if (cause instanceof InvalidFormatException ex) {
            return bindingAndMissingErrorsToMap(getField(ex), String.format(DEFAULT_BINDING_EXCEPTION_MESSAGE, ex.getValue()));
        }

        throw new RuntimeException(cause);
    }

    private String getField(InvalidFormatException e) {
        return e.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .findFirst()
                .orElse("unknown field");
    }

    /**
     *  Missing request parameter exception handler(필수 파라미터 누락)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.error("Request parameter missing exception: ", e);

        Map<String, String> errors = bindingAndMissingErrorsToMap(e.getParameterName(), String.format(DEFAULT_PARAMETER_MISSING_EXCEPTION_MESSAGE, e.getParameterName()));

        return ResponseEntity.badRequest()
                .body(createInputValidationResponse(errors));
    }

    /**
     * Request parameter binding exception handler(요청 파라미터, 경로 파라미터 바인딩 오류)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("Request parameter type mismatch exception :: ", e);

        Map<String, String> errors = bindingAndMissingErrorsToMap(e.getName(), String.format(DEFAULT_BINDING_EXCEPTION_MESSAGE, e.getValue()));

        return ResponseEntity.badRequest()
                .body(createInputValidationResponse(errors));
    }

    private Map<String, String> bindingAndMissingErrorsToMap(String key, String value) {
        return Map.of(key, value);
    }

    /**
     *  ModelAttribute binding exception handler(요청 파라미터 객체로 받을 경우, 바인딩 오류 및 Bean validation 검증기 오류)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e) {
        log.error("Request parameter bind exception(modelAttribute) :: ", e);

        Map<String, String> errors = bingResultErrorsToMap(e.getBindingResult());

        return ResponseEntity.badRequest()
                .body(createInputValidationResponse(errors));
    }

    /**
     *  Bean validation exception handler (Bean validation 검증기 오류)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Parameter validation exception :: ", e);

        Map<String, String> errors = bingResultErrorsToMap(e.getBindingResult());

        return ResponseEntity.badRequest()
                .body(createInputValidationResponse(errors));
    }

    private Map<String, String> bingResultErrorsToMap(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .collect(Collectors.toMap(
                        this::getField,
                        this::resolveErrorMessage
                ));
    }

    private String getField(ObjectError error) {
        if (error instanceof FieldError) {
            return extractFieldName(((FieldError) error).getField());
        }

        return extractFieldName(error.getObjectName());
    }

    private String extractFieldName(String errorFieldName) {
        int idxDot = errorFieldName.lastIndexOf(".");

        if (idxDot == -1) {
            return errorFieldName;
        }

        return errorFieldName.substring(idxDot + 1);
    }

    private String resolveErrorMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {

            if (fieldError.contains(TypeMismatchException.class)) {
                return String.format(DEFAULT_BINDING_EXCEPTION_MESSAGE, fieldError.getRejectedValue());
            }
        }

        return error.getDefaultMessage();
    }

    private ResponseEntity<?> createInputValidationResponse(Map<String, String> errors) {
        ResponseForm<Map<String, String>> response = ResponseForm.from(E90001, errors);

        return ResponseEntity.status(E90001.getHttpStatus())
                .body(response);
    }

    /**
     *  Custom exception handlers
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> handleGlobalException(GlobalException e) {
        log.error("e :: ", e);

        ResponseForm<Object> response = ResponseForm.of(e.getResponseCode());

        return ResponseEntity.status(e.getResponseCode().getHttpStatus())
                .body(response);
    }

    /**
     *  Java default exception handlers
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception e) {
        log.error("unexpected exception :: ", e);

        ResponseForm<Object> response = ResponseForm.of(E99999);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
