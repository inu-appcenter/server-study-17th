package study.server.domain.user.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.server.domain.user.controller.docs.UserControllerSpecification;
import study.server.domain.user.dto.LoginRequestDto;
import study.server.domain.user.dto.UserDto;
import study.server.domain.user.dto.UserResponseDto;
import study.server.domain.user.service.UserService;
import study.server.global.common.ResponseDto;
import study.server.domain.user.entity.CustomUserDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController implements UserControllerSpecification {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<ResponseDto<UserResponseDto>> userDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {

    UserResponseDto userDto = userService.getUserDetail(userDetails.getUsername());
    return ResponseEntity.ok(ResponseDto.success("유저 조회 성공", userDto));
  }

  @PostMapping("/register")
  public ResponseEntity<ResponseDto<Void>> register(@Valid @RequestBody UserDto userDto) {
    userService.registerUser(userDto);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(ResponseDto.success("유저 등록 성공", null));
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseDto<String>> login(@Valid @RequestBody LoginRequestDto requestDto) {
    String token = userService.login(requestDto);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(ResponseDto.success("회원 로그인 성공", token));
  }

  @PatchMapping
  public ResponseEntity<ResponseDto<Void>> updateName(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String userName) {
    userService.updateUserName(userDetails.getUser().getId(), userName);
    return ResponseEntity.ok(ResponseDto.success("유저 이름 수정 성공"));
  }

  @PutMapping
  public ResponseEntity<ResponseDto<Void>> update(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UserDto userDto) {
    userService.updateUserDetail(userDetails.getUser().getId(), userDto);
    return ResponseEntity.ok(ResponseDto.success("유저 정보 수정 성공"));
  }
}
