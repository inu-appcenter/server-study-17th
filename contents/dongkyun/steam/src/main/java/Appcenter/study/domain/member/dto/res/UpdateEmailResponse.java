package Appcenter.study.domain.member.dto.res;

import Appcenter.study.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "이메일 수정 응답 DTO")
public class UpdateEmailResponse {

    @Schema(description = "변경된 회원의 닉네임", example = "johndoe")
    private final String nickname;

    @Schema(description = "변경된 이메일 주소", example = "newemail@example.com")
    private final String email;

    @Builder
    private UpdateEmailResponse(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
