package com.example.reviewqueue.auth.service.dto;

import com.example.reviewqueue.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 30)
    private String password;

    @NotBlank
    @Size(min = 2, max = 20)
    private String nickname;

    public Member toEntity(String encodedPassword) {
        return new Member(email, encodedPassword, nickname);
    }
}
