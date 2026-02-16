package com.example.reviewqueue.studykeyword.domain;

import com.example.reviewqueue.common.domain.BaseEntity;
import com.example.reviewqueue.dailystudy.domain.DailyStudy;
import com.example.reviewqueue.studykeyword.exception.StudyKeywordException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static com.example.reviewqueue.common.response.ResponseCode.E15001;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_active = true")
@Entity
public class StudyKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  학습 키워드
     */
    private String keyword;

    /**
     *  학습 키워드 설명
     */
    private String description;

    @ManyToOne
    private DailyStudy dailyStudy;

    public StudyKeyword(String keyword, String description, DailyStudy dailyStudy) {
        this.keyword = keyword;
        this.description = description;
        this.dailyStudy = dailyStudy;
        dailyStudy.getKeywords().add(this);
    }

    public void updateKeywords(String keyword, String description) {
        validateKeyword(keyword);

        this.keyword = keyword;
        this.description = description;
    }

    private void validateKeyword(String keyword) {
        if (keyword.isBlank()) {
            throw new StudyKeywordException(E15001);
        }
    }
}
