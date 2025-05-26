package Appcenter.study.domain.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "리뷰 정보 응답 DTO")
public class ReviewResponse {

    @Schema(description = "리뷰 평점", example = "4")
    private final Integer reviewRating;

    @Schema(description = "리뷰 내용", example = "이 게임은 정말 훌륭합니다. 가격 대비 성능이 최고예요!")
    private final String reviewContent;

    @Schema(description = "리뷰가 작성된 시간", example = "2025-05-13T14:30:00")
    private final LocalDateTime reviewTime;

    @Schema(description = "리뷰를 작성한 회원의 닉네임", example = "newnewnickname")
    private final String memberNickname;

    @Schema(description = "리뷰를 작성한 회원의 프로필 이미지 URL", example = "https://example.com/profile.jpg")
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
