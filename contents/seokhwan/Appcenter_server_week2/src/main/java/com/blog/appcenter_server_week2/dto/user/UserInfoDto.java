package com.blog.appcenter_server_week2.dto.user;

import com.blog.appcenter_server_week2.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserInfoDto {

    private final String username;
    private final String nickname;
    private final String location;
    private final String profileUrl;

    @Builder
    private UserInfoDto(String username, String nickname, String location, String profileUrl) {
        this.username = username;
        this.nickname = nickname;
        this.location = location;
        this.profileUrl = profileUrl;
    }


    public static UserInfoDto from(User user) {
        return UserInfoDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .location(user.getLocation())
                .profileUrl(user.getProfile_url())
                .build();
    }
}
