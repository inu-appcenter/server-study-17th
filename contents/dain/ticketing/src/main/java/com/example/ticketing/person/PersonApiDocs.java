package com.example.ticketing.person;

import com.example.ticketing.exception.ErrorResponseEntity;
import com.example.ticketing.person.dto.req.*;
import com.example.ticketing.person.dto.res.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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
            @ApiResponse(responseCode = "400", description = "회원가입 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패했습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "409", description = "회원가입 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "ID 중복",
                                            value = "{\"error\" : \"409\", \"message\" : \"이미 존재하는 ID입니다\"}"

                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
    })
    ResponseEntity<PersonSignupResponseDto> signup(@Valid @RequestBody PersonSignupRequestDto requestDto);

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "비밀번호 불일치",
                                            value = "{\"error\" : \"401\", \"message\" : \"비밀번호가 일치하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "로그인 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "ID 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"조회할 수 없는 ID입니다\"}"
                                    )
                            }))
    })
    ResponseEntity<PersonLoginResponseDto> login(@Valid @RequestBody PersonLoginRequestDto loginRequest);

    @GetMapping
    @Operation(summary = "회원 개별 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "회원 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"회원을 조회할 수 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
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
            @ApiResponse(responseCode = "200", description = "회원 수정 성공"),
            @ApiResponse(responseCode = "404", description = "회원 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"사용자를 조회할 수 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "회원 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"error\" : \"403\", \"message\" : \"수정 권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<PersonUpdateResponseDto> update(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody PersonUpdateRequestDto updateRequest);

    @DeleteMapping
    @Operation(summary = "회원 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "회원 삭제 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"사용자를 조회할 수 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class))),
            @ApiResponse(responseCode = "403", description = "회원 삭제 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "권한 없음",
                                            value = "{\"error\" : \"403\", \"message\" : \"삭제 권한이 없습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponseEntity.class)))
    })
    ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails user);
}
