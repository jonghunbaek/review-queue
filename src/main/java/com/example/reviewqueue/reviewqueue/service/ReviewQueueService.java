package com.example.reviewqueue.reviewqueue.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.exception.DailyStudyException;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.reviewqueue.domain.ReviewCondition;
import com.example.reviewqueue.reviewqueue.domain.ReviewQueue;
import com.example.reviewqueue.reviewqueue.repository.ReviewQueueRepository;
import com.example.reviewqueue.reviewqueue.service.dto.ReviewQueueSave;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.*;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewQueueService {

    private final ReviewQueueRepository reviewQueueRepository;
    private final DailyStudyRepository dailyStudyRepository;

    // TODO :: Stream API로 로직 개선 필요
    public void save(ReviewQueueSave reviewQueueSave) {
        DailyStudy dailyStudy = findDailyStudyBy(reviewQueueSave.getDailyStudyId());
        ReviewCondition reviewCondition = reviewQueueSave.toReviewCondition(dailyStudy);

        List<ReviewQueue> reviewQueues = new ArrayList<>();
        LocalDate previousReviewDate = dailyStudy.getStudyDateTime().toLocalDate();
        for (int i = 0; i < reviewCondition.getReviewTimes(); i++) {
            Integer period = reviewCondition.getPeriods().get(i);
            LocalDate nextReviewDate = previousReviewDate.plusDays(period);

            ReviewQueue reviewQueue = new ReviewQueue(previousReviewDate, nextReviewDate, dailyStudy);
            reviewQueues.add(reviewQueue);

            previousReviewDate = nextReviewDate;
        }

        reviewQueueRepository.saveAll(reviewQueues);
    }

    private DailyStudy findDailyStudyBy(Long dailStudyId) {
        return dailyStudyRepository.findById(dailStudyId)
                .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailStudyId, E12000));
    }
}
