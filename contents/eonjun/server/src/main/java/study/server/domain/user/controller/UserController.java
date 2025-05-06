package study.server.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.server.domain.user.dto.LoginRequestDto;
import study.server.domain.user.dto.UserDto;
import study.server.domain.user.dto.UserResponseDto;
import study.server.domain.user.service.UserService;
import study.server.global.common.ApiResponse;
import study.server.global.security.CustomUserDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/detail")
  public ResponseEntity<ApiResponse<UserResponseDto>> userDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {

    UserResponseDto userDto = userService.getUserDetail(userDetails.getUsername());
    return ResponseEntity.ok(ApiResponse.success("유저 조회 성공", userDto));
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserDto userDto) {
    userService.registerUser(userDto);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(ApiResponse.success("유저 등록 성공", null));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequestDto requestDto) {
    String token = userService.login(requestDto);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(ApiResponse.success("유저 로그인 성공", token));
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @PatchMapping("/updateName")
  public ResponseEntity<ApiResponse<Void>> updateName(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String userName) {
    userService.updateUserName(userDetails.getUser().getId(), userName);
    return ResponseEntity.ok(ApiResponse.success("유저 이름 수정 성공"));
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @PutMapping("/update")
  public ResponseEntity<ApiResponse<Void>> update(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UserDto userDto) {
    userService.updateUserDetail(userDetails.getUser().getId(), userDto);
    return ResponseEntity.ok(ApiResponse.success("유저 정보 수정 성공"));
  }
}
