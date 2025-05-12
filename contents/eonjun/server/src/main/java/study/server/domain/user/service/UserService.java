package study.server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.server.domain.user.dto.LoginRequestDto;
import study.server.domain.user.dto.UserDto;
import study.server.domain.user.dto.UserResponseDto;
import study.server.domain.user.entity.User;
import study.server.domain.user.repository.UserRepository;
import study.server.global.exception.CustomException;
import study.server.global.security.CustomUserDetails;
import study.server.global.security.jwt.JwtTokenProvider;

import static study.server.global.exception.ErrorCode.EMAIL_DUPLICATED;
import static study.server.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  public UserResponseDto getUserDetail(String email) {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    return UserResponseDto.builder()
      .username(user.getUsername())
      .email(user.getEmail())
      .address(user.getAddress())
      .phone(user.getPhone())
      .role(user.getRole())
      .build();
  }

  @Transactional
  public void registerUser(UserDto userDto) {
    // 이메일 중복 확인
    if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new CustomException(EMAIL_DUPLICATED); // 이미 등록된 이메일이 있을 경우 예외 던짐
    }

    userRepository.save(User.builder()
      .username(userDto.getUsername())
      .email(userDto.getEmail())
      .password(passwordEncoder.encode(userDto.getPassword()))
      .address(userDto.getAddress())
      .phone(userDto.getPhone())
      .role(userDto.getRole())
      .build());
  }

  public String login(LoginRequestDto requestDto) {
    UsernamePasswordAuthenticationToken authenticationToken =
      new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());

    authenticationManager.authenticate(authenticationToken);

    // 인증이 성공하면 JWT 토큰을 생성하여 반환
    CustomUserDetails userDetails = userRepository.findByEmail(requestDto.getEmail())
      .map(CustomUserDetails::new)
      .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    return jwtTokenProvider.generateToken(userDetails);
  }

  @Transactional
  public void deleteUser(long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(USER_NOT_FOUND));  // 삭제할 유저가 없으면 예외 던짐

    userRepository.deleteById(userId);
  }

  @Transactional
  public void updateUserName(long userId, String userName) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(USER_NOT_FOUND));  // 유저가 없으면 예외 던짐

    user.updateUserName(userName);
  }

  @Transactional
  public void updateUserDetail(Long userId, UserDto userDto) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(USER_NOT_FOUND));  // 유저가 없으면 예외 던짐

    user.update(userDto);
  }
}
