package com.example.reviewqueue.study.repository;

import com.example.reviewqueue.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    List<Study> findAllByMemberId(Long memberId);
}
