package com.example.controller;

import com.example.DTO.UserResponse;
import com.example.DTO.UserSignupRequest;
import com.example.DTO.UserUpdateRequest;
import com.example.config.JwtTokenFilter;
import com.example.domain.user.User;
import com.example.security.JwtTokenProvider;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody UserSignupRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);


        String token = jwtTokenProvider.createToken(authentication.getName());

        // 헤더에 담아 리턴
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(token);
    }

    @PostMapping("/join")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserSignupRequest request) {
        User user = userService.CreateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.get(user));
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId,
                                           @RequestBody UserUpdateRequest request) {
        userService.UpdateUser(userId, request);
        return ResponseEntity.ok().build();
    }

}


