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
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordCheck())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        Member member = new Member(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()));
        log.info("Member {} created", member);

        memberRepository.save(member);

        return SignupResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean matchPassword = passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword());
        if (!matchPassword) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }
        log.info("Password Correct");

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthenticationToken();
        log.info("Member Authentication Token Created");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("Member Authentication Token Authenticated");

        if (!authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        LoginResponseDto loginResponseDto = jwtTokenProvider.generateToken(authentication);
        log.info("Member {} Login JWT Token Generated", member);

        return loginResponseDto;
    }

    @Transactional
    public UpdateMypageResponse updateMypage(UserDetailsImpl userDetails, UpdateMypageRequest updateMypageRequest) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        member.updateMypage(updateMypageRequest);
        log.info("updateMypage");

        return UpdateMypageResponse.builder().member(member).build();
    }

    @Transactional
    public UpdateEmailResponse updateEmail(UserDetailsImpl userDetails, UpdateEmailRequest updateEmailRequest) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        member.updateEmail(updateEmailRequest);
        log.info("updateEmail");

        return UpdateEmailResponse.builder().member(member).build();
    }

    @Transactional
    public Boolean checkEmailDuplicated(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }
        return true;
    }

    @Transactional
    public Boolean checkNicknameDuplicated(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }
        return true;
    }
}
