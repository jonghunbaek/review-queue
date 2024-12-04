package com.example.reviewqueue.member.service;

import com.example.reviewqueue.member.domain.Member;
import com.example.reviewqueue.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long registerIfAbsent(Long socialId, String email) {
        Member member = memberRepository.findByEmail(email).orElseGet(() ->
                memberRepository.save(new Member(socialId, email, createRandomNickname())));

        return member.getId();
    }

    private String createRandomNickname() {
        return UUID.randomUUID().toString();
    }
}
