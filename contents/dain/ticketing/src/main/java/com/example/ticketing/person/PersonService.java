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
import com.example.ticketing.security.CustomUserDetails;
import com.example.ticketing.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public PersonSignupResponseDto signup(PersonSignupRequestDto personSignupRequestDto) {
        if (personRepository.existsByLoginId(personSignupRequestDto.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        Person person = Person.builder()
                .loginId(personSignupRequestDto.getLoginId())
                .password(passwordEncoder.encode(personSignupRequestDto.getPassword()))
                .createdDate(LocalDateTime.now())
                .role(Role.ROLE_USER) // USER 권한 부여
                .build();
        personRepository.save(person);
        return PersonSignupResponseDto.from(person);
    }

    //로그인
    @Transactional
    public PersonLoginResponseDto login(PersonLoginRequestDto personLoginRequestDto) {
        Person person = personRepository.findByLoginId(personLoginRequestDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));

        if (!passwordEncoder.matches(personLoginRequestDto.getPassword(), person.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_LOGIN);
        }
        // 토큰 생성
        String token = jwtTokenProvider.generateAccessToken(person.getLoginId());
        return PersonLoginResponseDto.from(person, token);
    }

    //회원 정보 수정
    @Transactional
    public PersonUpdateResponseDto update(Long personId, PersonUpdateRequestDto dto) {
        // SecurityContextHolder에서 로그인 아이디 꺼내기
        String loginId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        // loginId로 Person 엔티티 조회
        Person p = personRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        // personId와 비교
        if (!p.getId().equals(personId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }
        Person existing = personRepository.findById(personId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        existing.update(dto);
        return PersonUpdateResponseDto.from(existing);
    }

    //전체 회원 조회
    @Transactional(readOnly = true)
    public List<PersonGetResponseDto> getPersonList() {
        return personRepository.findAll().stream() //Stream API 활용
                .map(PersonGetResponseDto::from)
                .toList();
    }

    //개별 회원 조회
    @Transactional(readOnly = true)
    public PersonGetResponseDto getPerson(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        return PersonGetResponseDto.from(person);
    }

    //회원 삭제
    @Transactional
    public void delete(Long personId) {
        // SecurityContextHolder에서 로그인 아이디 꺼내기
        String loginId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        // loginId로 Person 엔티티 조회
        Person p = personRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        // personId와 비교
        if (!p.getId().equals(personId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }
        Person existing = personRepository.findById(personId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        personRepository.delete(existing);
    }
}

