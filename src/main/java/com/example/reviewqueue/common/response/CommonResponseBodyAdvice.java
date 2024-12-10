package com.example.reviewqueue.common.response;

import com.example.reviewqueue.common.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static com.example.reviewqueue.common.response.ResponseCode.*;

@RestControllerAdvice
public class CommonResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();
        HttpStatus resolve = HttpStatus.resolve(status);

        if (body == null) {
            return ResponseForm.of(E00000);
        }

        if (body instanceof String) {
            return stringToJson(ResponseForm.from(E00000, body), response);
        }

        if (resolve == null) {
            return body;
        }

        if (!ObjectUtils.isEmpty(body) && body.getClass().equals(ResponseForm.class)) {
            return body;
        }

        if (resolve.is2xxSuccessful()) {
            return ResponseForm.from(E00000, body);
        }

        return body;
    }

    private Object stringToJson(ResponseForm<?> restApiResponse, ServerHttpResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            String json = objectMapper.writeValueAsString(restApiResponse);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return json;
        } catch (JsonProcessingException e){
            throw new GlobalException(E90000);
        }
    }
}
