package com.blog.appcenter_server_week2.service;

import com.blog.appcenter_server_week2.domain.entity.User;
import com.blog.appcenter_server_week2.domain.repository.UserRepository;
import com.blog.appcenter_server_week2.dto.user.LoginRequestDto;
import com.blog.appcenter_server_week2.dto.user.UserInfoDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupRequestDto;
import com.blog.appcenter_server_week2.dto.user.UserSignupResponseDto;
import com.blog.appcenter_server_week2.exception.CustomException;
import com.blog.appcenter_server_week2.exception.ErrorCode;
import com.blog.appcenter_server_week2.jwt.JwtTokenProvider;
import com.blog.appcenter_server_week2.jwt.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserSignupResponseDto signup(UserSignupRequestDto userSignupRequestDto) {
        userRepository.findByLoginId(userSignupRequestDto.getLoginId()).ifPresent(existingUser -> {
            throw new CustomException(ErrorCode.DUPLICATE_LOGINID);
        });
        User user = User.builder()
                .loginId(userSignupRequestDto.getLoginId())
                .username(userSignupRequestDto.getUsername())
                .password(passwordEncoder.encode(userSignupRequestDto.getPassword()))
                .nickname(userSignupRequestDto.getNickname())
                .location(userSignupRequestDto.getLocation())
                .profile_url(userSignupRequestDto.getProfileUrl())
                .build();

        User savedUser = userRepository.save(user);

        return UserSignupResponseDto.from(savedUser);
    }


    public UserSignupResponseDto updateUser(Long userId, UserSignupRequestDto userSignupRequestDto) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_ID));
        User saveUser = userRepository.save(existingUser.update(
                userSignupRequestDto.getLoginId(),
                userSignupRequestDto.getUsername(),
                passwordEncoder.encode(userSignupRequestDto.getPassword()),
                userSignupRequestDto.getNickname(),
                userSignupRequestDto.getLocation(),
                userSignupRequestDto.getProfileUrl()
        ));

        return UserSignupResponseDto.from(saveUser);
    }


    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        // 인증 처리
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword());
        Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(authentication);

        return new TokenResponseDto(token);
    }

    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_ID));
        return UserInfoDto.from(user);
    }
}
