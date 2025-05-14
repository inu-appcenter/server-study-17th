package com.example.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "사용자 정보 수정 요청 모델")
@Getter
public class UserUpdateRequest {

    @Schema(description = "변경할 비밀번호", example = "NewPassword123!")
    private String password;

    @Schema(description = "변경할 이름", example = "홍길순")
    private String name;

    @Schema(description = "변경할 전화번호", example = "010-5678-1234")
    private String phoneNumber;

    @Schema(description = "변경할 주소", example = "부산광역시 해운대구")
    private String address;
}
