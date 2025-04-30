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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signup(signupRequestDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.login(loginRequestDto));
    }

    // 마이페이지 수정
    @PutMapping("/mypage")
    public ResponseEntity<UpdateMypageResponse> updateMypage(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody UpdateMypageRequest updateMypageRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMypage(userDetails, updateMypageRequest));
    }

    // 이메일 변경
    @PatchMapping("/email/update")
    public ResponseEntity<UpdateEmailResponse> updateEmail(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody UpdateEmailRequest updateEmailRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateEmail(userDetails, updateEmailRequest));
    }

    // 이메일 중복 확인
    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> checkEmailDuplicated(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.checkEmailDuplicated(email));
    }

    // 닉네임 중복 확인
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<Boolean> checkNicknameDuplicated(@PathVariable String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.checkNicknameDuplicated(nickname));
    }
}
