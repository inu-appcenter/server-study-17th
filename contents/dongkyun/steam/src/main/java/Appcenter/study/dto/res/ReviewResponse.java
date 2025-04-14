package Appcenter.study.dto.res;

import Appcenter.study.entity.Member;
import Appcenter.study.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private final Integer reviewRating;
    private final String reviewContent;
    private final LocalDateTime reviewTime;
    private final String memberNickname;
    private final String memberProfileUrl;

    @Builder
    private ReviewResponse(Review review) {
        this.reviewRating = review.getRating();
        this.reviewContent = review.getContent();
        this.reviewTime = review.getCreateAt();
        this.memberNickname = review.getMember().getNickname();
        this.memberProfileUrl = review.getMember().getProfileUrl();
    }

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder().review(review).build();
    }
}
