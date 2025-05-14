package study.server.domain.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.server.domain.user.dto.LoginRequestDto;
import study.server.domain.user.dto.UserDto;
import study.server.domain.user.dto.UserResponseDto;
import study.server.global.common.ResponseDto;
import study.server.domain.user.entity.CustomUserDetails;

@Tag(name = "users", description = "회원 API")
public interface UserControllerSpecification {

  @Operation(summary = "회원 조회", description = "회원 조회를 수행하는 API")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "유저 조회 성공",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
  @GetMapping
  ResponseEntity<ResponseDto<UserResponseDto>> userDetail(@AuthenticationPrincipal CustomUserDetails userDetails);

  @Operation(
    summary = "회원 가입",
    description = "회원 가입을 수행하는 API",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "회원가입에 필요한 데이터",
      required = true,
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserDto.class)
      )
    )
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "회원 가입 성공",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserResponseDto.class))),
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    @ApiResponse(responseCode = "USER409_1", description = "이미 등록된 이메일입니다."),
    @ApiResponse(responseCode = "USER409_2", description = "이미 등록된 이름입니다.")
  })
  @PostMapping("/register")
  ResponseEntity<ResponseDto<Void>> register(@Valid @RequestBody UserDto userDto);

  @Operation(
    summary = "회원 로그인",
    description = "회원 로그인을 수행하는 API",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "회원 로그인에 필요한 데이터",
      required = true,
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = LoginRequestDto.class)
      )
    )
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "로그인 성공",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserResponseDto.class))),
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
  })
  @PostMapping("/login")
  ResponseEntity<ResponseDto<String>> login(@Valid @RequestBody LoginRequestDto requestDto);

  @Operation(
    summary = "회원 이름 수정",
    description = "회원 이름을 수정하는 API"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "이름 수정 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    @ApiResponse(responseCode = "USER409_2", description = "이미 등록된 이름입니다.")
  })
  @PatchMapping
  ResponseEntity<ResponseDto<Void>> updateName(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String userName);

  @Operation(
    summary = "회원 정보 수정",
    description = "회원 정보를 수정하는 API",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "회원 정보 수정에 필요한 데이터",
      required = true,
      content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = UserDto.class)
      )
    )
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    @ApiResponse(responseCode = "409", description = "중복된 데이터")
  })
  @PutMapping
  ResponseEntity<ResponseDto<Void>> update(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UserDto userDto);
  }
