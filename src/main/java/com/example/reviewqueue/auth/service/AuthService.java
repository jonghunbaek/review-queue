package com.example.reviewqueue.auth.service;

import com.example.reviewqueue.auth.exception.AuthException;
import com.example.reviewqueue.auth.service.dto.LoginRequest;
import com.example.reviewqueue.auth.service.dto.SignupRequest;
import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.reviewqueue.common.response.ResponseCode.E00008;
import static com.example.reviewqueue.common.response.ResponseCode.E00009;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signUp(SignupRequest request) {
        validateDuplicateEmail(request.getEmail());

        return memberRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())))
                .getId();
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) throw new AuthException(E00008);
    }

    public Long signIn(LoginRequest request) {
        Member member = findMemberByEmail(request.getEmail());
        validatePassword(request.getPassword(), member.getPassword());

        return member.getId();
    }

    private void validatePassword(String rawPassword, String password) {
        if (!passwordEncoder.matches(rawPassword, password)) throw new AuthException(E00009);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(E00009));
    }
}
