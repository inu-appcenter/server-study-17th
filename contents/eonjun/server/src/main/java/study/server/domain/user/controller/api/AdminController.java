package study.server.domain.user.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.server.domain.user.service.UserService;
import study.server.global.common.ResponseDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/delete")
  public ResponseEntity<ResponseDto<Void>> delete(@RequestParam Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(ResponseDto.success("유저 삭제 성공"));
  }
}

