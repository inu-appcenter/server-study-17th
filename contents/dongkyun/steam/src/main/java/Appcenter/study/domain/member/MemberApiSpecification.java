package Appcenter.study.domain.member;

import Appcenter.study.domain.member.dto.req.LoginRequestDto;
import Appcenter.study.domain.member.dto.req.SignupRequestDto;
import Appcenter.study.domain.member.dto.req.UpdateEmailRequest;
import Appcenter.study.domain.member.dto.req.UpdateMypageRequest;
import Appcenter.study.domain.member.dto.res.LoginResponseDto;
import Appcenter.study.domain.member.dto.res.SignupResponseDto;
import Appcenter.study.domain.member.dto.res.UpdateEmailResponse;
import Appcenter.study.domain.member.dto.res.UpdateMypageResponse;
import Appcenter.study.global.security.jwt.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberApiSpecification {

    @Operation(
            summary = "회원가입 [ JWT ❌ ]",
            description = "회원가입을 진행합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(schema = @Schema(implementation = SignupResponseDto.class))
                    )
            }
    )
    @PostMapping
    ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto);

    @Operation(
            summary = "로그인 [ JWT ❌ ]",
            description = "이메일과 비밀번호로 로그인을 시도합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "로그인 실패 - 잘못된 자격 증명")
            }
    )
    @PostMapping
    ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto);

    @Operation(
            summary = "마이페이지 수정",
            description = "로그인한 사용자의 마이페이지 정보를 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "마이페이지 수정 성공",
                            content = @Content(schema = @Schema(implementation = UpdateMypageResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    @PutMapping
    ResponseEntity<UpdateMypageResponse> updateMypage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateMypageRequest updateMypageRequest);

    @Operation(
            summary = "이메일 변경",
            description = "로그인한 사용자의 이메일을 변경합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "이메일 변경 성공",
                            content = @Content(schema = @Schema(implementation = UpdateEmailResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    @PatchMapping("/email/update")
    ResponseEntity<UpdateEmailResponse> updateEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateEmailRequest updateEmailRequest);

    @Operation(
            summary = "이메일 중복 확인 [ JWT ❌ ]",
            description = "이메일이 이미 존재하는지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용 가능 여부 반환"),
                    @ApiResponse(responseCode = "400", description = "이메일이 중복되었습니다.")
            }
    )
    @GetMapping("/email/{email}")
    ResponseEntity<Boolean> checkEmailDuplicated(@PathVariable String email);

    @Operation(
            summary = "닉네임 중복 확인 [ JWT ❌ ]",
            description = "닉네임이 이미 존재하는지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용 가능 여부 반환"),
                    @ApiResponse(responseCode = "400", description = "닉네임이 중복되었습니다.")
            }
    )
    @GetMapping("/nickname/{nickname}")
    ResponseEntity<Boolean> checkNicknameDuplicated(@PathVariable String nickname);
}
