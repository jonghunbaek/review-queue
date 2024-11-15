package com.example.reviewqueue.review.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.exception.DailyStudyException;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.review.domain.ReviewCondition;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.review.service.dto.ReviewData;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DailyStudyRepository dailyStudyRepository;

    public void save(ReviewQueueSave reviewQueueSave) {
        DailyStudy dailyStudy = findDailyStudyBy(reviewQueueSave.getDailyStudyId());
        ReviewCondition reviewCondition = reviewQueueSave.toReviewCondition(dailyStudy);

        LocalDate startDate = dailyStudy.getStudyDateTime().toLocalDate();
        List<Review> reviews = reviewCondition.getReviewPeriods().stream()
                .reduce(new ArrayList<>(List.of(new Review(null, startDate, dailyStudy))),
                        (queues, period) -> addNextReview(queues, period, dailyStudy),
                        (a, b) -> a
                );

        // 첫 번째 요소는 초기값이므로 제거
        reviews.remove(0);

        reviewRepository.saveAll(reviews);
    }

    private ArrayList<Review> addNextReview(ArrayList<Review> reviews, int period, DailyStudy dailyStudy) {
        LocalDate lastReviewDate = reviews.get(reviews.size() - 1).getReviewDate();
        LocalDate nextReviewDate = lastReviewDate.plusDays(period);

        reviews.add(new Review(lastReviewDate, nextReviewDate, dailyStudy));

        return reviews;
    }

    public List<Long> findMemberIdsByReviewDate(LocalDate reviewDate) {
        List<Review> reviews = reviewRepository.findAllByReviewDate(reviewDate);

        return reviews.stream()
                .map(review -> review.getMember().getId())
                .distinct()
                .toList();
    }

    // TODO :: 현재로선 불필요한 메서드 -> 추후 전체 조회가 아닌 사용자 아이디별로 조회할 수 있도록 변경(반환 타입은 그대로)
    public List<ReviewData> findAllReviewDataByDate(LocalDate reviewDate) {
        List<Review> reviews = reviewRepository.findAllByReviewDate(reviewDate);

        return reviews.stream()
                .map(ReviewData::of)
                .toList();
    }

    private DailyStudy findDailyStudyBy(Long dailStudyId) {
        return dailyStudyRepository.findById(dailStudyId)
                .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailStudyId, E12000));
    }
}
