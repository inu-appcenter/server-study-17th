package Appcenter.study.domain.member;

import Appcenter.study.domain.member.dto.req.LoginRequestDto;
import Appcenter.study.domain.member.dto.req.SignupRequestDto;
import Appcenter.study.domain.member.dto.req.UpdateEmailRequest;
import Appcenter.study.domain.member.dto.req.UpdateMypageRequest;
import Appcenter.study.domain.member.dto.res.LoginResponseDto;
import Appcenter.study.domain.member.dto.res.SignupResponseDto;
import Appcenter.study.domain.member.dto.res.UpdateEmailResponse;
import Appcenter.study.domain.member.dto.res.UpdateMypageResponse;
import Appcenter.study.global.exception.CustomException;
import Appcenter.study.global.exception.ErrorCode;
import Appcenter.study.global.security.jwt.JwtTokenProvider;
import Appcenter.study.global.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        log.info("[회원가입 요청] email={}, nickname={}", signupRequestDto.getEmail(), signupRequestDto.getNickname());

        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordCheck())) {
            log.warn("[회원가입 실패] 비밀번호 불일치: email={}", signupRequestDto.getEmail());
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        Member member = new Member(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()));

        memberRepository.save(member);

        log.info("[회원가입 완료] memberId={}, email={}", member.getId(), member.getEmail());

        return SignupResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        log.info("[로그인 요청] email={}", loginRequestDto.getEmail());

        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> {
                    log.warn("[로그인 실패] 사용자 없음: email={}", loginRequestDto.getEmail());
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            log.warn("[로그인 실패] 비밀번호 불일치: email={}", loginRequestDto.getEmail());
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        log.info("[로그인 성공] email={}", member.getEmail());

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthenticationToken();
        log.debug("Spring Security 인증 시작");

        // Spring Security 인증 수행
        // 내부적으로 UserDetailsService 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 상태 확인
        if (!authentication.isAuthenticated()) {
            log.error("[로그인 실패] 인증 실패: email={}", loginRequestDto.getEmail());
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        // JWT 토큰 생성
        LoginResponseDto loginResponseDto = jwtTokenProvider.generateToken(authentication);
        log.info("[JWT 발급 완료] memberId={}, email={}", member.getId(), member.getEmail());

        return loginResponseDto;
    }

    @Transactional
    public UpdateMypageResponse updateMypage(UserDetailsImpl userDetails, UpdateMypageRequest updateMypageRequest) {

        log.info("[마이페이지 수정 요청] memberId={}", userDetails.getMember().getId());

        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> {
                    log.warn("[마이페이지 수정 실패] 사용자 없음: memberId={}", userDetails.getMember().getId());
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });

        member.updateMypage(updateMypageRequest);
        log.info("[마이페이지 수정 완료] memberId={}", member.getId());

        return UpdateMypageResponse.builder().member(member).build();
    }

    @Transactional
    public UpdateEmailResponse updateEmail(UserDetailsImpl userDetails, UpdateEmailRequest updateEmailRequest) {

        log.info("[이메일 변경 요청] memberId={}, newEmail={}", userDetails.getMember().getId(), updateEmailRequest.getEmail());

        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> {
                    log.warn("[이메일 변경 실패] 사용자 없음: memberId={}", userDetails.getMember().getId());
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });

        member.updateEmail(updateEmailRequest);
        log.info("[이메일 변경 완료] memberId={}, email={}", member.getId(), member.getEmail());

        return UpdateEmailResponse.builder().member(member).build();
    }

    @Transactional(readOnly = true)
    public Boolean checkEmailDuplicated(String email) {

        log.info("[이메일 중복 확인] email={}", email);

        if (memberRepository.existsByEmail(email)) {
            log.warn("[중복 이메일] email={}", email);
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public Boolean checkNicknameDuplicated(String nickname) {

        log.info("[닉네임 중복 확인] nickname={}", nickname);

        if (memberRepository.existsByNickname(nickname)) {
            log.warn("[중복 닉네임] nickname={}", nickname);
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }
        return true;
    }
}
