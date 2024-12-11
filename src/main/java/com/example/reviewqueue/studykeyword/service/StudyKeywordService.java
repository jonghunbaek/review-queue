package com.example.reviewqueue.studykeyword.service;

import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.studykeyword.repository.StudyKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyKeywordService {

    private final DailyStudyRepository dailyStudyRepository;
    private final StudyKeywordRepository keywordRepository;
}
