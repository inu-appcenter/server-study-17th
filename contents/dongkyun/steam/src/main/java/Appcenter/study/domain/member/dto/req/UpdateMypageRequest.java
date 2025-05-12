package Appcenter.study.domain.member.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
public class UpdateMypageRequest {

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    @NotBlank(message = "위치가 비어있습니다.")
    private String location;

    @URL(message = "유효한 URL 형식이 아닙니다.")
    private String profileUrl;
}
