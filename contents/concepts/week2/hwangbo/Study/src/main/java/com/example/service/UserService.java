package com.example.service;

import com.example.DTO.UserLogInRequest;
import com.example.DTO.UserResponse;
import com.example.DTO.UserSignupRequest;
import com.example.DTO.UserUpdateRequest;
import com.example.domain.user.User;
import com.example.domain.user.UserRepository;
import com.example.exception.CustomException;
import com.example.exception.ErrorCode;
import com.example.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserSignupRequest request) {
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress()
        );

        return userRepository.save(user);
    }

    @PreAuthorize("#userId == authentication.principal.id")
    public void updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        user.UpdateInfo(passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress());
    }

    public UserResponse logIn(UserLogInRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);


        String token = jwtTokenProvider.createToken(authentication.getName());

//        // 헤더에 담아 리턴 -> 토큰을 바디에 실어서 보내니까 필요 없는 코드라네요
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Authorization", "Bearer " + token);

        return new UserResponse(token);
    }

}
