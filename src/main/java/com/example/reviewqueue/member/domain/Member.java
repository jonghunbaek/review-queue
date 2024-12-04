package com.example.reviewqueue.member.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long socialId;

    private String email;

    private String nickname;

    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public Member(Long socialId, String email, String nickname) {
        this.socialId = socialId;
        this.email = email;
        this.nickname = nickname;
    }
}
