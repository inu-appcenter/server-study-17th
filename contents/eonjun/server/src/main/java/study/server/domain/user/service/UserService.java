package study.server.domain.user.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.server.domain.user.dto.LoginRequestDto;
import study.server.domain.user.dto.UserDto;
import study.server.domain.user.dto.UserResponseDto;
import study.server.domain.user.entity.User;
import study.server.domain.user.repository.UserRepository;
import study.server.global.security.CustomUserDetails;
import study.server.global.security.jwt.JwtTokenProvider;

import java.util.Optional;

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
      .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));

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
      .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

    return jwtTokenProvider.generateToken(userDetails);
  }

  @Transactional
  public void deleteUser(long userId) {
    userRepository.deleteById(userId);
  }

  @Transactional
  public void updateUserName(long userId, String userName) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

    user.updateUserName(userName);
  }

  @Transactional
  public void updateUserDetail(Long userId, UserDto userDto) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

    user.update(userDto);
  }
}
