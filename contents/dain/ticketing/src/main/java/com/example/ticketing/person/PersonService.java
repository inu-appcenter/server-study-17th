package com.example.ticketing.person;

import com.example.ticketing.person.dto.req.PersonLoginRequestDto;
import com.example.ticketing.person.dto.req.PersonSignupRequestDto;
import com.example.ticketing.person.dto.req.PersonUpdateRequestDto;
import com.example.ticketing.person.dto.res.PersonGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    //회원가입
    @Transactional
    public Person signup(PersonSignupRequestDto personSignupRequestDto) {
        if(personRepository.existsByLoginId(personSignupRequestDto.getLoginId())){
            throw new RuntimeException("이미 존재하는 ID입니다.");
        }

        Person person = Person.builder()
                .loginId(personSignupRequestDto.getLoginId())
                .password(personSignupRequestDto.getPassword())
                .createdDate(LocalDateTime.now())
                .build();
        return personRepository.save(person);
    }

    //로그인
    @Transactional
    public Person login(PersonLoginRequestDto personLoginRequestDto) {
        Person person = personRepository.findByLoginId(personLoginRequestDto.getLoginId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 ID입니다."));

        if (!person.getPassword().equals(personLoginRequestDto.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return person;
    }

    //회원 정보(전체) 수정
    @Transactional
    public Person update(Long personId, PersonUpdateRequestDto personUpdateRequestDto) {
        Person existingPerson = personRepository.findById(personId)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 ID입니다."));
        existingPerson.update(personUpdateRequestDto); // 기존 인스턴스의 필드 직접 업데이트
        return personRepository.save(existingPerson);
    }

    //전체 회원 조회
    @Transactional(readOnly = true)
    public List<PersonGetResponseDto> getPersonList() {
        List<Person> personEntities = personRepository.findAll();
        List<PersonGetResponseDto> personListResponse = new ArrayList<>();
        for (Person person : personEntities) {
            personListResponse.add(PersonGetResponseDto.from(person));
        }
        return personListResponse;
    }

    //개별 회원 조회
    @Transactional(readOnly = true)
    public Person getPerson(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다."));
    }

    //회원 삭제
    @Transactional
    public void delete(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다."));
        personRepository.delete(person);
    }
}

