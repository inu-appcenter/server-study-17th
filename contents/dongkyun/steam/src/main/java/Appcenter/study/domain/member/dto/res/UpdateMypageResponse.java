package Appcenter.study.domain.member.dto.res;

import Appcenter.study.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "이메일 수정 응답 DTO")
public class UpdateMypageResponse {

    @Schema(description = "변경된 회원의 닉네임", example = "newnewnickname")
    private final String nickname;

    @Schema(description = "변경된 사용자의 활동 위치 또는 지역", example = "서울시 강남구")
    private final String location;

    @Schema(description = "변경된 프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private final String profileUrl;

    @Builder
    private UpdateMypageResponse(Member member) {
        this.nickname = member.getNickname();
        this.location = member.getLocation();
        this.profileUrl = member.getProfileUrl();
    }
}
