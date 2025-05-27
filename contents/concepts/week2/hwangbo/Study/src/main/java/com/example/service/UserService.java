package com.example.service;

import com.example.dto.user.UserLogInRequest;
import com.example.dto.user.UserResponse;
import com.example.dto.user.UserSignupRequest;
import com.example.dto.user.UserUpdateRequest;
import com.example.domain.user.User;
import com.example.domain.user.UserRepository;
import com.example.exception.CustomException;
import com.example.exception.ErrorCode;
import com.example.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserSignupRequest request) {
        log.info("회원 가입 요청 → email={}", request.getEmail());

        // 이메일 중복 검증
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 비밀번호 재입력 검증
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress()
        );
        User saved = userRepository.save(user);
        log.info("회원 가입 완료 → userId={}", saved.getId());
        return saved;
    }

    @PreAuthorize("#userId == authentication.principal.id")
    public void updateUser(Long userId, UserUpdateRequest request) {
        log.info("회원 정보 수정 요청 → userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.UpdateInfo(
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress()
        );
        log.info("회원 정보 수정 완료 → userId={}", userId);
    }


    public UserResponse logIn(UserLogInRequest request) {
        log.info("로그인 시도 → email={}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtTokenProvider.createToken(authentication.getName());
        log.info("로그인 성공 → email={}, token 발급 완료", request.getEmail());
        return new UserResponse(token);
    }

}
