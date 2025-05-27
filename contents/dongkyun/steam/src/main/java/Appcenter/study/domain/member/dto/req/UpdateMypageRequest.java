package Appcenter.study.domain.member.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
@Schema(description = "마이페이지 수정 요청 DTO")
public class UpdateMypageRequest {

    @NotBlank(message = "닉네임이 비어있습니다.")
    @Schema(description = "수정할 닉네임", example = "newnickname")
    private String nickname;

    @NotBlank(message = "위치가 비어있습니다.")
    @Schema(description = "사용자의 활동 위치 또는 지역", example = "서울시 강남구")
    private String location;

    @URL(message = "유효한 URL 형식이 아닙니다.")
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileUrl;
}
