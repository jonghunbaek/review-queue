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

import static com.example.reviewqueue.common.response.ResponseCode.E12000;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewQueueService {

    private final ReviewQueueRepository reviewQueueRepository;
    private final DailyStudyRepository dailyStudyRepository;

    public void save(ReviewQueueSave reviewQueueSave) {
        DailyStudy dailyStudy = findDailyStudyBy(reviewQueueSave.getDailyStudyId());
        ReviewCondition reviewCondition = reviewQueueSave.toReviewCondition(dailyStudy);

        LocalDate startDate = dailyStudy.getStudyDateTime().toLocalDate();
        List<ReviewQueue> reviewQueues = reviewCondition.getReviewPeriods().stream()
                .reduce(new ArrayList<>(List.of(new ReviewQueue(null, startDate, dailyStudy))),
                        (queues, period) -> addNextReview(queues, period, dailyStudy),
                        (a, b) -> a
                );

        // 첫 번째 요소는 초기값이므로 제거
        reviewQueues.remove(0);

        reviewQueueRepository.saveAll(reviewQueues);
    }

    private ArrayList<ReviewQueue> addNextReview(ArrayList<ReviewQueue> reviewQueues, int period, DailyStudy dailyStudy) {
        LocalDate lastReviewDate = reviewQueues.get(reviewQueues.size() - 1).getReviewDate();
        LocalDate nextReviewDate = lastReviewDate.plusDays(period);

        reviewQueues.add(new ReviewQueue(lastReviewDate, nextReviewDate, dailyStudy));

        return reviewQueues;
    }

    private DailyStudy findDailyStudyBy(Long dailStudyId) {
        return dailyStudyRepository.findById(dailStudyId)
                .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailStudyId, E12000));
    }
}
