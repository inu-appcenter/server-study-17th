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

    //회원 정보(전체) 수정
    @Transactional
    public PersonUpdateResponseDto update(Long personId, PersonUpdateRequestDto personUpdateRequestDto) {
        Person existingPerson = personRepository.findById(personId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        existingPerson.update(personUpdateRequestDto);
        return PersonUpdateResponseDto.from(existingPerson);
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
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        personRepository.delete(person);
    }
}

