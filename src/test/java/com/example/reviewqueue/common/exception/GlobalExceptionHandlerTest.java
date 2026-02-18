package com.example.reviewqueue.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.example.reviewqueue.common.response.ResponseCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();
    }

    @DisplayName("잘못된 JSON body 전송 시 400과 E90001 응답을 반환한다")
    @Test
    void handleHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(post("/test/body-binding")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E90001.name()))
                .andExpect(jsonPath("$.message").value(E90001.getMessage()))
                .andExpect(jsonPath("$.body").isMap());
    }

    @DisplayName("필수 @RequestParam 누락 시 400과 E90001 응답을 반환한다")
    @Test
    void handleMissingServletRequestParameterException() throws Exception {
        mockMvc.perform(get("/test/missing-param"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E90001.name()))
                .andExpect(jsonPath("$.message").value(E90001.getMessage()))
                .andExpect(jsonPath("$.body.query").isString());
    }

    @DisplayName("@PathVariable 타입 불일치 시 400과 E90001 응답을 반환한다")
    @Test
    void handleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/test/type-mismatch/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E90001.name()))
                .andExpect(jsonPath("$.message").value(E90001.getMessage()))
                .andExpect(jsonPath("$.body.id").isString());
    }

    @DisplayName("@ModelAttribute 바인딩 실패 시 400과 E90001 응답을 반환한다")
    @Test
    void handleBindException() throws Exception {
        mockMvc.perform(get("/test/model-attribute")
                        .param("count", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E90001.name()))
                .andExpect(jsonPath("$.message").value(E90001.getMessage()))
                .andExpect(jsonPath("$.body.count").isString());
    }

    @DisplayName("@Valid 검증 실패 시 400과 E90001 응답을 반환한다")
    @Test
    void handleMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E90001.name()))
                .andExpect(jsonPath("$.message").value(E90001.getMessage()))
                .andExpect(jsonPath("$.body.name").isString());
    }

    @DisplayName("ConstraintViolationException 발생 시 400과 E90001 응답을 반환한다")
    @Test
    void handleConstraintViolationException() throws Exception {
        mockMvc.perform(get("/test/constraint-violation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E90001.name()))
                .andExpect(jsonPath("$.message").value(E90001.getMessage()))
                .andExpect(jsonPath("$.body").isMap());
    }

    @DisplayName("필수 쿠키 누락 시 401과 E00001 응답을 반환한다")
    @Test
    void handleMissingRequestCookieException() throws Exception {
        mockMvc.perform(get("/test/missing-cookie"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(E00001.name()))
                .andExpect(jsonPath("$.message").value(E00001.getMessage()))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @DisplayName("등록되지 않은 경로 요청 시 404와 E90002 응답을 반환한다")
    @Test
    void handleNoResourceFoundException() throws Exception {
        mockMvc.perform(get("/test/no-resource"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(E90002.name()))
                .andExpect(jsonPath("$.message").value(E90002.getMessage()))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @DisplayName("AuthorizationDeniedException 발생 시 403과 E00002 응답을 반환한다")
    @Test
    void handleAuthorizationDeniedException() throws Exception {
        mockMvc.perform(get("/test/authorization-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(E00002.name()))
                .andExpect(jsonPath("$.message").value(E00002.getMessage()))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @DisplayName("IllegalArgumentException 발생 시 400과 E90001 응답을 반환한다")
    @Test
    void handleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E90001.name()))
                .andExpect(jsonPath("$.message").value(E90001.getMessage()))
                .andExpect(jsonPath("$.body").value("잘못된 인자입니다"));
    }

    @DisplayName("IllegalStateException 발생 시 500과 E99999 응답을 반환한다")
    @Test
    void handleIllegalStateException() throws Exception {
        mockMvc.perform(get("/test/illegal-state"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(E99999.name()))
                .andExpect(jsonPath("$.message").value(E99999.getMessage()))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @DisplayName("GlobalException 발생 시 해당 ResponseCode의 HTTP 상태와 응답을 반환한다")
    @Test
    void handleGlobalException() throws Exception {
        mockMvc.perform(get("/test/global-exception"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(E11000.name()))
                .andExpect(jsonPath("$.message").value(E11000.getMessage()))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @DisplayName("예상하지 못한 예외 발생 시 500과 E99999 응답을 반환한다")
    @Test
    void handleUnexpectedException() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(E99999.name()))
                .andExpect(jsonPath("$.message").value(E99999.getMessage()))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    // -- 테스트 전용 내부 컨트롤러 및 DTO --

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @PostMapping("/body-binding")
        void bodyBinding(@RequestBody TestRequest request) {
        }

        @GetMapping("/missing-param")
        void missingParam(@RequestParam String query) {
        }

        @GetMapping("/type-mismatch/{id}")
        void typeMismatch(@PathVariable Long id) {
        }

        @GetMapping("/model-attribute")
        void modelAttribute(@ModelAttribute ModelAttributeRequest request) {
        }

        @PostMapping("/validation")
        void validation(@Valid @RequestBody TestRequest request) {
        }

        @GetMapping("/missing-cookie")
        void missingCookie(@CookieValue String token) {
        }

        @GetMapping("/no-resource")
        void noResource() throws NoResourceFoundException {
            throw new NoResourceFoundException(HttpMethod.GET, "/test/no-resource", null);
        }

        @GetMapping("/constraint-violation")
        void constraintViolation() {
            ConstraintViolation<?> violation = mock("fieldName", "제약 조건을 위반했습니다");
            throw new ConstraintViolationException(Set.of(violation));
        }

        @GetMapping("/authorization-denied")
        void authorizationDenied() {
            throw new AuthorizationDeniedException("Access denied");
        }

        @GetMapping("/illegal-argument")
        void illegalArgument() {
            throw new IllegalArgumentException("잘못된 인자입니다");
        }

        @GetMapping("/illegal-state")
        void illegalState() {
            throw new IllegalStateException("잘못된 상태입니다");
        }

        @GetMapping("/global-exception")
        void globalException() {
            throw new GlobalException(E11000);
        }

        @GetMapping("/unexpected")
        void unexpected() throws Exception {
            throw new Exception("예상하지 못한 예외");
        }

        private static ConstraintViolation<?> mock(String propertyPath, String message) {
            return new ConstraintViolation<>() {
                @Override public String getMessage() { return message; }
                @Override public String getMessageTemplate() { return null; }
                @Override public Object getRootBean() { return null; }
                @Override public Class getRootBeanClass() { return null; }
                @Override public Object getLeafBean() { return null; }
                @Override public Object[] getExecutableParameters() { return new Object[0]; }
                @Override public Object getExecutableReturnValue() { return null; }
                @Override public jakarta.validation.Path getPropertyPath() {
                    return new jakarta.validation.Path() {
                        @Override public String toString() { return propertyPath; }
                        @Override public java.util.Iterator<Node> iterator() { return java.util.Collections.emptyIterator(); }
                    };
                }
                @Override public Object getInvalidValue() { return null; }
                @Override public jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor() { return null; }
                @Override public <U> U unwrap(Class<U> type) { return null; }
            };
        }
    }

    static class TestRequest {
        @NotBlank
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    static class ModelAttributeRequest {
        private int count;

        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}
