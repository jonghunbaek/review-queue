package com.example.reviewqueue.oauth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoAuthCode {

    private String code;

    private String state;

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

}
