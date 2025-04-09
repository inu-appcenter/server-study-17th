package com.example.controller;

import com.example.DTO.UserResponse;
import com.example.DTO.UserSignupRequest;
import com.example.DTO.UserUpdateRequest;
import com.example.domain.user.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponse> CreateUser(@RequestBody UserSignupRequest request) {
        User user = userService.CreateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.Get(user));
    }

    @PatchMapping("update/{userId}")
    public ResponseEntity<Void> UpdateUser(@PathVariable Long userId,
                                           @RequestBody UserUpdateRequest request) {
        userService.UpdateUser(userId, request);
        return ResponseEntity.ok().build();
    }

}


