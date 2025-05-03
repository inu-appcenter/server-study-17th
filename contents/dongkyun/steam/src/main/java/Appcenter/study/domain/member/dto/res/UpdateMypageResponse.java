package Appcenter.study.domain.member.dto.res;

import Appcenter.study.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMypageResponse {

    private final String nickname;
    private final String location;
    private final String profileUrl;

    @Builder
    private UpdateMypageResponse(Member member) {
        this.nickname = member.getNickname();
        this.location = member.getLocation();
        this.profileUrl = member.getProfileUrl();
    }
}
