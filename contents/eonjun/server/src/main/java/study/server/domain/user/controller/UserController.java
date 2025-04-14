package study.server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.server.domain.user.dto.UserDto;
import study.server.domain.user.service.UserService;
import study.server.global.common.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/detail")
  public ResponseEntity<ApiResponse<UserDto>> userDetail(@RequestParam Long userId) {
    UserDto userDto = userService.getUserDetail(userId);
    return ResponseEntity.ok(ApiResponse.success("유저 조회 성공", userDto));
  }

  /**
   * RequestBody 사용
   * @param userDto
   * @return
   */
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserDto userDto) {
    userService.registerUser(userDto);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(ApiResponse.success("유저 등록 성공", null));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ApiResponse<Void>> delete(@RequestParam Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(ApiResponse.success("유저 삭제 성공"));
  }

  @PatchMapping("/updateName")
  public ResponseEntity<ApiResponse<Void>> updateName(@RequestParam Long userId, @RequestParam String userName) {
    userService.updateUserName(userId, userName);
    return ResponseEntity.ok(ApiResponse.success("유저 이름 수정 성공"));
  }

  @PutMapping("/update")
  public ResponseEntity<ApiResponse<Void>> update(@RequestParam Long userId, @RequestBody UserDto userDto) {
    userService.updateUserDetail(userId, userDto);
    return ResponseEntity.ok(ApiResponse.success("유저 정보 수정 성공"));
  }
}
