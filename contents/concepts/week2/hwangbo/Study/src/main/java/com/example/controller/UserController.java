package com.example.controller;

import com.example.DTO.UserLogInRequest;
import com.example.DTO.UserResponse;
import com.example.DTO.UserSignupRequest;
import com.example.DTO.UserUpdateRequest;
import com.example.domain.user.User;
import com.example.security.CustomUserDetail;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> logIn(@RequestBody @Valid UserLogInRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.logIn(request));
    }

    @PostMapping("/join")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserSignupRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.get(user));
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                           @RequestBody @Valid UserUpdateRequest request) {
        userService.updateUser(customUserDetail.getId(), request);
        return ResponseEntity.ok().build();
    }

}


