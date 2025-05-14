package com.example.controller;

import com.example.DTO.user.UserLogInRequest;
import com.example.DTO.user.UserResponse;
import com.example.DTO.user.UserSignupRequest;
import com.example.DTO.user.UserUpdateRequest;
import com.example.domain.user.User;
import com.example.security.CustomUserDetail;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "사용자 로그인하고 JWT 토큰을 반환")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> logIn(
            @Parameter(description = "로그인 정보", required = true)
            @RequestBody @Valid UserLogInRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.logIn(request));
    }

    @Operation(summary = "회원가입", description = "새 사용자 계정을 생성")
    @PostMapping("/join")
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "회원가입 정보", required = true)
            @RequestBody @Valid UserSignupRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.get(user));
    }

    @Operation(summary = "회원 정보 수정", description = "인증된 사용자의 프로필 정보를 업데이트")
    @PatchMapping
    public ResponseEntity<Void> updateUser(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @Parameter(description = "업데이트할 사용자 정보", required = true)
            @RequestBody @Valid UserUpdateRequest request) {
        userService.updateUser(customUserDetail.getId(), request);
        return ResponseEntity.ok().build();
    }
}


