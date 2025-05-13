package com.example.ticketing.person;

import com.example.ticketing.exception.CustomException;
import com.example.ticketing.exception.ErrorCode;
import com.example.ticketing.person.dto.req.PersonLoginRequestDto;
import com.example.ticketing.person.dto.req.PersonSignupRequestDto;
import com.example.ticketing.person.dto.req.PersonUpdateRequestDto;
import com.example.ticketing.person.dto.res.PersonGetResponseDto;
import com.example.ticketing.person.dto.res.PersonLoginResponseDto;
import com.example.ticketing.person.dto.res.PersonSignupResponseDto;
import com.example.ticketing.person.dto.res.PersonUpdateResponseDto;
import com.example.ticketing.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public PersonSignupResponseDto signup(PersonSignupRequestDto personSignupRequestDto) {
        log.info("[회원가입 시도] loginId: {}", personSignupRequestDto.getLoginId());
        if (personRepository.existsByLoginId(personSignupRequestDto.getLoginId())) {
            log.warn("[회원가입 실패] 중복된 loginId: {}", personSignupRequestDto.getLoginId());
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        Person person = Person.builder()
                .loginId(personSignupRequestDto.getLoginId())
                .password(passwordEncoder.encode(personSignupRequestDto.getPassword()))
                .createdDate(LocalDateTime.now())
                .role(Role.ROLE_USER) // USER 권한 부여
                .grade(Grade.WELCOME) // 최초 등급 WELCOME 부여
                .build();
        personRepository.save(person);
        log.info("[회원가입 성공] loginId: {}", person.getLoginId());
        return PersonSignupResponseDto.from(person);
    }

    //로그인
    @Transactional
    public PersonLoginResponseDto login(PersonLoginRequestDto personLoginRequestDto) {
        log.info("[로그인 시도] loginId: {}", personLoginRequestDto.getLoginId());
        Person person = personRepository.findByLoginId(personLoginRequestDto.getLoginId())
                .orElseThrow(() -> {
                    log.warn("[로그인 실패] 존재하지 않는 ID: {}", personLoginRequestDto.getLoginId());
                    return new CustomException(ErrorCode.ID_NOT_FOUND);
                });

        if (!passwordEncoder.matches(personLoginRequestDto.getPassword(), person.getPassword())) {
            log.warn("[로그인 실패] 비밀번호 불일치 - loginId: {}", personLoginRequestDto.getLoginId());
            throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
        }
        // 토큰 생성
        String token = jwtTokenProvider.generateAccessToken(person.getLoginId());
        log.info("[로그인 성공] loginId: {}", person.getLoginId());
        return PersonLoginResponseDto.from(person, token);
    }

    //회원 정보 수정
    @Transactional
    public PersonUpdateResponseDto update(UserDetails user, PersonUpdateRequestDto dto) {
        //loginId 꺼내기
        String username = user.getUsername();
        log.info("[회원정보 수정 시도] loginId: {}", username);

        //loginId로 조회
        Person existing = personRepository.findByLoginId(username)
                        .orElseThrow(() -> {
                            log.warn("[회원정보 수정 실패] 존재하지 않는 ID: {}", username);
                            return new CustomException(ErrorCode.ID_NOT_FOUND);
                        });
        existing.update(dto);
        log.info("[회원정보 수정 성공] loginId: {}", username);
        return PersonUpdateResponseDto.from(existing);
    }

    //전체 회원 조회
    @Transactional(readOnly = true)
    public List<PersonGetResponseDto> getPersonList() {
        return personRepository.findAll().stream() //Stream API 활용 -> findAll() 대신 동적쿼리(JPQL)로 DB에서 가져오기
                .map(PersonGetResponseDto::from)
                .toList();
    }

    //개별 회원 조회
    @Transactional(readOnly = true)
    public PersonGetResponseDto getPerson(UserDetails user) {
        String username = user.getUsername();
        Person existing = personRepository.findByLoginId(username)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        return PersonGetResponseDto.from(existing);
    }

    //회원 삭제
    @Transactional
    public void delete(UserDetails user) {
        String username = user.getUsername();
        log.info("[회원 삭제 시도] loginId: {}", username);
        Person existing = personRepository.findByLoginId(username)
                        .orElseThrow(() -> {
                            log.warn("[회원 삭제 실패] 존재하지 않는 ID: {}", username);
                            return new CustomException(ErrorCode.ID_NOT_FOUND);
                        });
        personRepository.delete(existing);
        log.info("[회원 삭제 성공] loginId: {}", username);
    }
}

