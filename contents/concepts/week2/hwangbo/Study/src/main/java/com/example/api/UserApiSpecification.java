package com.example.api;

import com.example.dto.user.UserLogInRequest;
import com.example.dto.user.UserResponse;
import com.example.dto.user.UserSignupRequest;
import com.example.dto.user.UserUpdateRequest;
import com.example.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserApiSpecification {

    @Operation(summary = "로그인", description = "사용자 로그인하고 JWT 토큰을 반환")
    ResponseEntity<UserResponse> logIn(
            @RequestBody @Valid UserLogInRequest request
    );

    @Operation(summary = "회원가입", description = "새 사용자 계정을 생성")
    ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid UserSignupRequest request
    );

    @Operation(summary = "회원 정보 수정", description = "인증된 사용자의 프로필 정보를 업데이트")
    ResponseEntity<Void> updateUser(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @RequestBody @Valid UserUpdateRequest request
    );
}
