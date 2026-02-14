package com.example.reviewqueue.auth.service;

import com.example.reviewqueue.auth.exception.AuthException;
import com.example.reviewqueue.auth.service.dto.LoginRequest;
import com.example.reviewqueue.auth.service.dto.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @DisplayName("이메일과 비밀번호로 회원가입을 하면 멤버 ID를 반환한다.")
    @Test
    void signUp() {
        // given
        SignupRequest request = new SignupRequest("test@email.com", "password123", "테스터");

        // when
        Long memberId = authService.signUp(request);

        // then
        assertThat(memberId).isNotNull();
    }

    @DisplayName("이미 등록된 이메일로 회원가입하면 예외가 발생한다.")
    @Test
    void signUp_duplicateEmail() {
        // given
        SignupRequest request = new SignupRequest("test@email.com", "password123", "테스터");
        authService.signUp(request);

        // when & then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AuthException.class);
    }

    @DisplayName("이메일과 비밀번호로 로그인하면 멤버 ID를 반환한다.")
    @Test
    void signIn() {
        // given
        SignupRequest signupRequest = new SignupRequest("test@email.com", "password123", "테스터");
        Long savedMemberId = authService.signUp(signupRequest);

        LoginRequest loginRequest = new LoginRequest("test@email.com", "password123");

        // when
        Long memberId = authService.signIn(loginRequest);

        // then
        assertThat(memberId).isEqualTo(savedMemberId);
    }

    @DisplayName("존재하지 않는 이메일로 로그인하면 예외가 발생한다.")
    @Test
    void signIn_emailNotFound() {
        // given
        LoginRequest request = new LoginRequest("notexist@email.com", "password123");

        // when & then
        assertThatThrownBy(() -> authService.signIn(request))
                .isInstanceOf(AuthException.class);
    }

    @DisplayName("잘못된 비밀번호로 로그인하면 예외가 발생한다.")
    @Test
    void signIn_wrongPassword() {
        // given
        SignupRequest signupRequest = new SignupRequest("test@email.com", "password123", "테스터");
        authService.signUp(signupRequest);

        LoginRequest loginRequest = new LoginRequest("test@email.com", "wrongpassword");

        // when & then
        assertThatThrownBy(() -> authService.signIn(loginRequest))
                .isInstanceOf(AuthException.class);
    }
}
