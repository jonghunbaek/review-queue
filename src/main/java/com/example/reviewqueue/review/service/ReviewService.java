package com.example.reviewqueue.review.service;

import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.dailystudy.exception.DailyStudyException;
import com.example.reviewqueue.dailystudy.repository.DailyStudyRepository;
import com.example.reviewqueue.review.domain.Review;
import com.example.reviewqueue.review.domain.ReviewCondition;
import com.example.reviewqueue.review.exception.ReviewException;
import com.example.reviewqueue.review.repository.ReviewRepository;
import com.example.reviewqueue.review.service.dto.ReviewData;
import com.example.reviewqueue.review.service.dto.ReviewGeneralInfo;
import com.example.reviewqueue.review.service.dto.ReviewQueueSave;
import com.example.reviewqueue.review.service.dto.ReviewsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.reviewqueue.common.response.ResponseCode.E12000;
import static com.example.reviewqueue.common.response.ResponseCode.E13003;
import static com.example.reviewqueue.common.util.GlobalValidator.validateAccessPermission;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DailyStudyRepository dailyStudyRepository;

    public void save(ReviewQueueSave reviewQueueSave, Long memberId) {
        DailyStudy dailyStudy = findDailyStudyBy(reviewQueueSave.getDailyStudyId());

        validateAccessPermission(memberId, dailyStudy.getStudy().getMember().getId());

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

    /**
     *  해당 일에 복습을 해야하는 사용자의 아이디를 모두 찾아 반환한다.
     */
    public List<Long> findMemberIdsByReviewDate(LocalDate reviewDate) {
        List<Review> reviews = reviewRepository.findAllByReviewDate(reviewDate);

        return reviews.stream()
                .map(review -> review.getMember().getId())
                .distinct()
                .toList();
    }

    /**
     *  단일 복습 조회를 하면 해당 복습을 완료한 것으로 처리한다.
     */
    public ReviewData findById(Long reviewId, Long memberId) {
        Review review = reviewRepository.findByIdAndIsCompletedIsFalse(reviewId)
                .orElseThrow(() -> new ReviewException("reviewId :: " + reviewId, E13003));

        validateAccessPermission(memberId, review.getMember().getId());
        review.completeReview();

        return ReviewData.from(review, isLastReview(review));
    }

    private boolean isLastReview(Review review) {
        DailyStudy dailyStudy = review.getDailyStudy();
        List<Review> reviews = reviewRepository.findAllByDailyStudyId(dailyStudy.getId());

        return reviews.size() == 1;
    }

    public List<ReviewGeneralInfo> findAllReviewGeneralInfo(LocalDate reviewDate, Long memberId) {
        List<Review> reviews = reviewRepository.findAllByReviewDateAndMemberId(reviewDate, memberId);

        return reviews.stream()
                .map(ReviewGeneralInfo::of)
                .toList();
    }

    public ReviewsData findAllReviewDataBy(LocalDate reviewDate, Long dailyStudyId, Long memberId) {
        List<Review> reviews = reviewRepository.findAllByReviewDateAndDailyStudyIdAndMemberId(reviewDate, dailyStudyId, memberId);

        validateAccessPermission(memberId, reviews.get(0).getMember().getId());

        return createReviewData(reviews, reviewDate, memberId);
    }

    private ReviewsData createReviewData(List<Review> reviews, LocalDate reviewDate, Long memberId) {
        List<ReviewData> data = reviews.stream()
                .map(ReviewData::of)
                .toList();

        return new ReviewsData(memberId, reviewDate, data);
    }

    private DailyStudy findDailyStudyBy(Long dailStudyId) {
        return dailyStudyRepository.findById(dailStudyId)
                .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailStudyId, E12000));
    }
}
