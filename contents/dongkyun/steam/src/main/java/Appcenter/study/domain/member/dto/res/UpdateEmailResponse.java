package Appcenter.study.domain.member.dto.res;

import Appcenter.study.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateEmailResponse {

    private final String nickname;
    private final String email;

    @Builder
    private UpdateEmailResponse(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
