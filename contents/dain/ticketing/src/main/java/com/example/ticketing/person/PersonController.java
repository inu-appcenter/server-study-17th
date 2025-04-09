package com.example.ticketing.person;

import com.example.ticketing.person.dto.req.PersonLoginRequestDto;
import com.example.ticketing.person.dto.req.PersonSignupRequestDto;
import com.example.ticketing.person.dto.req.PersonUpdateRequestDto;
import com.example.ticketing.person.dto.res.PersonGetResponseDto;
import com.example.ticketing.person.dto.res.PersonLoginResponseDto;
import com.example.ticketing.person.dto.res.PersonSignupResponseDto;
import com.example.ticketing.person.dto.res.PersonUpdateResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    @PostMapping("/signup")
    public ResponseEntity<PersonSignupResponseDto> signup(@Valid @RequestBody PersonSignupRequestDto signupRequest) {
        Person person = personService.signup(signupRequest);
        PersonSignupResponseDto responseDto = PersonSignupResponseDto.from(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<PersonLoginResponseDto> login(@Valid @RequestBody PersonLoginRequestDto loginRequest) {
        Person person = personService.login(loginRequest);
        PersonLoginResponseDto responseDto = PersonLoginResponseDto.from(person);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonGetResponseDto> getPerson(@PathVariable Long id) {
        Person person = personService.getPerson(id);
        PersonGetResponseDto responseDto = PersonGetResponseDto.from(person);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<PersonGetResponseDto>> getPersonList() {
        List<PersonGetResponseDto> responseList = personService.getPersonList();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonUpdateResponseDto> update(@PathVariable Long id, @Valid @RequestBody PersonUpdateRequestDto updateRequest) {
        Person updatedPerson = personService.update(id, updateRequest);
        PersonUpdateResponseDto responseDto = PersonUpdateResponseDto.from(updatedPerson);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
