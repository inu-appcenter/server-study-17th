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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserSignupResponseDto signup(UserSignupRequestDto userSignupRequestDto) {
        log.info("회원가입 시작 - 아이디: {}", userSignupRequestDto.getLoginId());

        // 중복 아이디 확인 및 예외 처리
        userRepository.findByLoginId(userSignupRequestDto.getLoginId()).ifPresent(existingUser -> {
            log.warn("회원가입 실패 - 이미 존재하는 아이디: {}", userSignupRequestDto.getLoginId());
            throw new CustomException(ErrorCode.DUPLICATE_LOGINID);
        });

        // 사용자 객체 생성
        User user = User.builder()
                .loginId(userSignupRequestDto.getLoginId())
                .username(userSignupRequestDto.getUsername())
                .password(passwordEncoder.encode(userSignupRequestDto.getPassword()))
                .nickname(userSignupRequestDto.getNickname())
                .location(userSignupRequestDto.getLocation())
                .profile_url(userSignupRequestDto.getProfileUrl())
                .build();

        // 사용자 저장
        User savedUser = userRepository.save(user);
        log.info("회원가입 완료 - 아이디: {}, 사용자명: {}", savedUser.getLoginId(), savedUser.getUsername());

        return UserSignupResponseDto.from(savedUser);
    }


    public void updateUser(Long userId, UserSignupRequestDto userSignupRequestDto) {
        log.info("사용자 정보 업데이트 시작 - 사용자 ID: {}", userId);

        // 사용자 존재 여부 확인
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("사용자 정보 업데이트 실패 - 존재하지 않는 사용자 ID: {}", userId);
                    return new CustomException(ErrorCode.NOT_EXIST_ID);
                });


        try {
            log.debug("사용자 정보 업데이트 - 이전 정보: 아이디={}, 사용자명={}", existingUser.getLoginId(), existingUser.getUsername());

            existingUser.update(
                    userSignupRequestDto.getLoginId(),
                    userSignupRequestDto.getUsername(),
                    passwordEncoder.encode(userSignupRequestDto.getPassword()),
                    userSignupRequestDto.getNickname(),
                    userSignupRequestDto.getLocation(),
                    userSignupRequestDto.getProfileUrl()
            );

            log.info("사용자 정보 업데이트 완료 - 사용자 ID: {}, 새 닉네임: {}", userId, userSignupRequestDto.getNickname());
        } catch (DataAccessException e) {
            // DB 관련 예외 처리
            log.error("사용자 정보 업데이트 중 데이터베이스 오류 - 사용자 ID: {}", userId, e);
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }


    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        String loginId = loginRequestDto.getLoginId();
        log.info("로그인 시도 - 아이디: {}", loginId);

        try {
            // 인증 처리
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword());
            log.debug("인증 객체 생성 완료 - 아이디: {}", loginId);

            // 인증 매니저를 통한 인증 시도
            Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("인증 성공 - 아이디: {}", authentication.getName());

            // JWT 토큰 생성
            String token = jwtTokenProvider.createToken(authentication);
            log.debug("JWT 토큰 생성 완료 - 아이디: {}", loginId);

            log.info("로그인 성공 - 아이디: {}", loginId);
            return new TokenResponseDto(token);
        } catch (BadCredentialsException e){
            // 잘못된 자격 증명(아이디/비밀번호 불일치)
            log.warn("로그인 실패 - 잘못된 자격 증명 - 아이디: {}", loginId);
            throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
        }
    }

    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(String loginId) {
        log.info("사용자 정보 조회 시작 - 아이디: {}", loginId);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    log.warn("사용자 정보 조회 실패 - 존재하지 않는 아이디: {}", loginId);
                    return new CustomException(ErrorCode.NOT_EXIST_ID);
                });

        log.debug("사용자 정보 조회 결과 - 아이디: {}, 사용자명: {}, 닉네임: {}", user.getLoginId(), user.getUsername(), user.getNickname());

        UserInfoDto userInfoDto = UserInfoDto.from(user);
        log.info("사용자 정보 조회 완료 - 아이디: {}", loginId);

        return userInfoDto;
    }
}
