package com.blog.appcenter_server_week2.controller;

import com.blog.appcenter_server_week2.dto.user.UserSignupRequestDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupResponseDto;
import com.blog.appcenter_server_week2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userservice;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        UserSignupResponseDto newUserSignupResponseDto = userservice.signup(userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserSignupResponseDto);
    }

    @PatchMapping("/signup/{userId}")
    public ResponseEntity<UserSignupResponseDto> update(@PathVariable Long userId, @RequestBody UserSignupRequestDto userSignupRequestDto) {
        UserSignupResponseDto updateUser = userservice.updateUser(userId, userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

}
