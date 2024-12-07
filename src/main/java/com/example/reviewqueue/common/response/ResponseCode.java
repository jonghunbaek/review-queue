package com.example.reviewqueue.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/*
 E0 ~ : Auth;
 E1 ~ : Domain
    E10 ~ : Member
    E11 ~ : Study
    E12 ~ : DailyStudy
    E13 ~ : Review
    E14 ~ : ReviewReminder
 E2 ~ : Repository
 E3 ~ : Service
 E4 ~ : Controller
 E5 ~ : Servlet, Interceptor
 E9 ~ : Etc
 */
@Getter
public enum ResponseCode {

    E00000("OK", OK),
    E00001("토큰이 존재하지 않거나 허가되지 않은 접근입니다.", UNAUTHORIZED),
    E00002("해당 페이지에 접근 권한이 없습니다.", FORBIDDEN),
    E00003("토큰이 만료됐습니다.", UNAUTHORIZED),
    E00004("토큰이 위조됐습니다.", UNAUTHORIZED),
    E00005("해당 토큰은 사용 불가한 토큰입니다.", UNAUTHORIZED),
    E00006("토큰의 형식이 잘못됐습니다.", UNAUTHORIZED),
    E00007("해당 리소스에 대한 접근 권한이 없습니다.", UNAUTHORIZED),

    E10000("조회할 수 있는 회원이 없습니다.", BAD_REQUEST),

    E11000("조회할 수 있는 학습이 없습니다.", BAD_REQUEST),

    E12000("조회할 수 있는 일일 학습이 없습니다.",BAD_REQUEST),

    E13000("복습 횟수의 범위가 잘못 지정됐습니다.", BAD_REQUEST),
    E13001("복습 주기의 범위가 잘못 지정됐습니다.", BAD_REQUEST),
    E13002("복습 횟수와 복습 주기 개수가 일치하지 않습니다.", BAD_REQUEST),

    E14000("이벤트 발송 중 문제가 발생했습니다.", SERVICE_UNAVAILABLE),
    E14001("확인하지 않은 알림이 있습니다.", OK),
    E14002("SSE 연결이 생성되었습니다.", OK)
    ;

    private String message;
    private HttpStatus httpStatus;

    ResponseCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
