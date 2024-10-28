package com.example.reviewqueue.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/*
 E0 ~ : Auth;
 E1 ~ : Domain
    E10 ~ : Member
    E11 ~ : Study
    E12 ~ : DailyStudy
    E13 ~ : Review
 E2 ~ : Repository
 E3 ~ : Service
 E4 ~ : Controller
 E5 ~ : Servlet, Interceptor
 E9 ~ : Etc
 */
@Getter
public enum ResponseCode {

    E13000("복습 횟수의 범위가 잘못 지정됐습니다.", BAD_REQUEST),
    E13001("복습 주기의 범위가 잘못 지정됐습니다.", BAD_REQUEST);

    private String message;
    private HttpStatus httpStatus;

    ResponseCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
