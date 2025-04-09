package study.server.domain.user.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.server.domain.user.dto.UserDto;
import study.server.domain.user.entity.User;
import study.server.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserDto getUserDetail(long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

    return UserDto.builder()
      .username(user.getUsername())
      .email(user.getEmail())
      .password(user.getPassword())
      .address(user.getAddress())
      .phone(user.getPhone())
      .build();
  }

  @Transactional
  public void registerUser(UserDto userDto) {
    userRepository.save(User.builder()
      .username(userDto.getUsername())
      .email(userDto.getEmail())
      .password(userDto.getPassword())
      .address(userDto.getAddress())
      .phone(userDto.getPhone())
      .build());
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
