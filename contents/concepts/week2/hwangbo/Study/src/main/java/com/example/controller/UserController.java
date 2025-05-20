package com.example.controller;

import com.example.api.UserApiSpecification;
import com.example.domain.user.User;
import com.example.dto.user.UserLogInRequest;
import com.example.dto.user.UserResponse;
import com.example.dto.user.UserSignupRequest;
import com.example.dto.user.UserUpdateRequest;
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
public class UserController implements UserApiSpecification {

    private final UserService userService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<UserResponse> logIn(@RequestBody @Valid UserLogInRequest request) {
        return ResponseEntity.ok(userService.logIn(request));
    }

    @Override
    @PostMapping("/join")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserSignupRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.get(user));
    }

    @Override
    @PatchMapping
    public ResponseEntity<Void> updateUser(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @RequestBody @Valid UserUpdateRequest request) {
        userService.updateUser(customUserDetail.getId(), request);
        return ResponseEntity.ok().build();
    }
}
