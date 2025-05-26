package com.example.ticketing.person;

import com.example.ticketing.person.dto.req.*;
import com.example.ticketing.person.dto.res.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/persons")
public interface PersonApiDocs {

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "요청값 오류"),
            @ApiResponse(responseCode = "409", description = "중복된 ID")
    })
    ResponseEntity<PersonSignupResponseDto> signup(@Valid @RequestBody PersonSignupRequestDto requestDto);

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    ResponseEntity<PersonLoginResponseDto> login(@Valid @RequestBody PersonLoginRequestDto loginRequest);

    @GetMapping
    @Operation(summary = "회원 개별 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    ResponseEntity<PersonGetResponseDto> getPerson(@AuthenticationPrincipal UserDetails user);

    @GetMapping("/list")
    @Operation(summary = "회원 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 리스트 조회 성공")
    })
    ResponseEntity<List<PersonGetResponseDto>> getPersonList();

    @PutMapping
    @Operation(summary = "회원 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 수정 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    ResponseEntity<PersonUpdateResponseDto> update(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody PersonUpdateRequestDto updateRequest);

    @DeleteMapping
    @Operation(summary = "회원 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails user);
}
