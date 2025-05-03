package study.server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.server.domain.user.service.UserService;
import study.server.global.common.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/delete")
  public ResponseEntity<ApiResponse<Void>> delete(@RequestParam Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(ApiResponse.success("유저 삭제 성공"));
  }
}

