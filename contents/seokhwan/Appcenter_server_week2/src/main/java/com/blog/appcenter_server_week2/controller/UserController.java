package com.blog.appcenter_server_week2.controller;

import com.blog.appcenter_server_week2.dto.user.LoginRequestDto;
import com.blog.appcenter_server_week2.dto.user.UserInfoDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupRequestDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupResponseDto;
import com.blog.appcenter_server_week2.jwt.TokenResponseDto;
import com.blog.appcenter_server_week2.jwt.UserDetailsImpl;
import com.blog.appcenter_server_week2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userservice;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {
        UserSignupResponseDto newUserSignupResponseDto = userservice.signup(userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserSignupResponseDto);
    }

    @Operation(summary = "유저 정보 수정")
    @PatchMapping
    public ResponseEntity<Void> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {
        userservice.updateUser(userDetails.getUser().getId(), userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponse = userservice.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @Operation(summary = "내 정보")
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        UserInfoDto userInfo = userservice.getUserInfo(userDetailsImpl.getUsername());
        return ResponseEntity.ok(userInfo);
    }

}
