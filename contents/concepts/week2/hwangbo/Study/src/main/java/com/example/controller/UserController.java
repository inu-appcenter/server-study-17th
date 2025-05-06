package com.example.controller;

import com.example.DTO.UserLogInRequest;
import com.example.DTO.UserResponse;
import com.example.DTO.UserSignupRequest;
import com.example.DTO.UserUpdateRequest;
import com.example.domain.user.User;
import com.example.security.JwtTokenProvider;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal User user,
                                           @RequestBody @Valid UserUpdateRequest request) {
        userService.updateUser(user.getId(), request);
        return ResponseEntity.ok().build();
    }

}


