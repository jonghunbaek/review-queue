package com.example.reviewqueue.common.exception;

import com.example.reviewqueue.common.response.ResponseForm;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import static com.example.reviewqueue.common.response.ResponseCode.*;
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

        return Map.of("body", "요청 본문의 형식이 잘못됐거나 존재하지 않습니다.");
    }

    private String getField(InvalidFormatException e) {
        return e.getPath().stream()
                .map(JacksonException.Reference::getPropertyName)
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

    private ResponseForm<Map<String, String>> createInputValidationResponse(Map<String, String> errors) {
        return ResponseForm.from(E90001, errors);
    }

    /**
     *  Missing request cookie exception handler(필수 쿠키 누락)
     */
    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<?> handleMissingRequestCookie(MissingRequestCookieException e) {
        log.error("Missing request cookie exception :: ", e);

        return ResponseEntity.status(E00001.getHttpStatus())
                .body(ResponseForm.of(E00001));
    }

    /**
     *  Constraint violation exception handler(제약 조건 위반)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint violation exception :: ", e);

        Map<String, String> errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> extractFieldName(violation.getPropertyPath().toString()),
                        ConstraintViolation::getMessage
                ));

        return ResponseEntity.badRequest()
                .body(createInputValidationResponse(errors));
    }

    /**
     *  No resource found exception handler(존재하지 않는 경로 요청)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("No resource found exception :: ", e);

        return ResponseEntity.status(E90002.getHttpStatus())
                .body(ResponseForm.of(E90002));
    }

    /**
     *  Authorization denied exception handler(접근 권한 없음)
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error("Authorization denied exception :: ", e);

        return ResponseEntity.status(E00002.getHttpStatus())
                .body(ResponseForm.of(E00002));
    }

    /**
     *  Illegal argument exception handler(잘못된 인자)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument exception :: ", e);

        return ResponseEntity.badRequest()
                .body(ResponseForm.from(E90001, e.getLocalizedMessage()));
    }

    /**
     *  Illegal state exception handler(잘못된 상태)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
        log.error("Illegal state exception :: ", e);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ResponseForm.of(E99999));
    }

    /**
     *  Custom exception handlers
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> handleGlobalException(GlobalException e) {
        logCustomException(e);

        return ResponseEntity.status(e.getResponseCode().getHttpStatus())
                .body(ResponseForm.of(e.getResponseCode()));
    }

    private void logCustomException(GlobalException e) {
        log.error("GlobalException :: responseCode={}, detailMessage={}", e.getResponseCode(), e.getDetailMessage(), e);
    }

    /**
     *  Java default exception handlers
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception e, WebRequest request) {
        log.error("unexpected exception :: requestURI={}", request.getDescription(false), e);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ResponseForm.of(E99999));
    }
}
