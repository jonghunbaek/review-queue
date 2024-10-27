package com.example.reviewqueue.member.repository;

import com.example.reviewqueue.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
