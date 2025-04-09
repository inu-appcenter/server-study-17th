package Appcenter.study.controller;

import Appcenter.study.dto.req.UpdateEmailRequest;
import Appcenter.study.dto.req.UpdateMypageRequest;
import Appcenter.study.dto.res.UpdateEmailResponse;
import Appcenter.study.dto.res.UpdateMypageResponse;
import Appcenter.study.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 마이페이지 수정
    @PutMapping("/mypage/update/{memberId}")
    public ResponseEntity<UpdateMypageResponse> updateMypage(@PathVariable Long memberId, @Valid @RequestBody UpdateMypageRequest updateMypageRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMypage(memberId, updateMypageRequest));
    }

    // 이메일 변경
    @PatchMapping("/email/update/{memberId}")
    public ResponseEntity<UpdateEmailResponse> updateEmail(@PathVariable Long memberId, @Valid @RequestBody UpdateEmailRequest updateEmailRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateEmail(memberId, updateEmailRequest));
    }
}
