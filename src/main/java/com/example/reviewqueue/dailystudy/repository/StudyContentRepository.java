package com.example.reviewqueue.dailystudy.repository;

import com.example.reviewqueue.dailystudy.domain.StudyContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyContentRepository extends JpaRepository<StudyContent, Long> {
}
