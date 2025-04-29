package com.blog.appcenter_server_week2.controller;

import com.blog.appcenter_server_week2.dto.user.LoginRequestDto;
import com.blog.appcenter_server_week2.dto.user.LoginResponseDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupRequestDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupResponseDto;
import com.blog.appcenter_server_week2.jwt.JwtTokenProvider;
import com.blog.appcenter_server_week2.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userservice;
    private final AuthenticationManagerBuilder authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword());
        Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

}
