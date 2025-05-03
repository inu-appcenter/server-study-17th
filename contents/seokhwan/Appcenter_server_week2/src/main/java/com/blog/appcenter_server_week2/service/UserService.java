package com.blog.appcenter_server_week2.service;

import com.blog.appcenter_server_week2.config.SecurityConfig;
import com.blog.appcenter_server_week2.domain.entity.Product;
import com.blog.appcenter_server_week2.domain.entity.User;
import com.blog.appcenter_server_week2.domain.repository.UserRepository;
import com.blog.appcenter_server_week2.dto.user.LoginRequestDto;
import com.blog.appcenter_server_week2.dto.user.LoginResponseDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupRequestDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    public UserSignupResponseDto signup(UserSignupRequestDto userSignupRequestDto) {
        //예외처리 생략
        User user = User.builder()
                .loginId(userSignupRequestDto.getLoginId())
                .username(userSignupRequestDto.getUsername())
                .password(securityConfig.passwordEncoder().encode(userSignupRequestDto.getPassword()))
                .nickname(userSignupRequestDto.getNickname())
                .location(userSignupRequestDto.getLocation())
                .profile_url(userSignupRequestDto.getProfileUrl())
                .build();

        User savedUser = userRepository.save(user);

        return UserSignupResponseDto.from(savedUser);
    }


    public UserSignupResponseDto updateUser(Long userId, UserSignupRequestDto userSignupRequestDto) {
        //예외처리 생략
        User existingUser = userRepository.findById(userId).orElse(null);

        User saveUser = userRepository.save(existingUser.update(
                userSignupRequestDto.getLoginId(),
                userSignupRequestDto.getUsername(),
                securityConfig.passwordEncoder().encode(userSignupRequestDto.getPassword()),
                userSignupRequestDto.getNickname(),
                userSignupRequestDto.getLocation(),
                userSignupRequestDto.getProfileUrl()
        ));

        return UserSignupResponseDto.from(saveUser);
    }


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByLoginId(loginRequestDto.getLoginId()).orElse(null);
        if (!securityConfig.passwordEncoder().matches(loginRequestDto.getPassword(), user.getPassword()))
            throw new RuntimeException("Incorrect password");
        return new LoginResponseDto(loginRequestDto.getLoginId());
    }
}
