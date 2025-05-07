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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    @PostMapping("/signup")
    public ResponseEntity<PersonSignupResponseDto> signup(@Valid @RequestBody PersonSignupRequestDto requestDto) {
        return ResponseEntity.ok(personService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<PersonLoginResponseDto> login(@Valid @RequestBody PersonLoginRequestDto loginRequest) {
        return ResponseEntity.ok(personService.login(loginRequest));
    }

    @GetMapping
    public ResponseEntity<PersonGetResponseDto> getPerson(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(personService.getPerson(user));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PersonGetResponseDto>> getPersonList() {
        List<PersonGetResponseDto> responseList = personService.getPersonList();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PutMapping
    public ResponseEntity<PersonUpdateResponseDto> update(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody PersonUpdateRequestDto updateRequest) {
        return ResponseEntity.ok(personService.update(user, updateRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails user) {
        personService.delete(user);
        return ResponseEntity.noContent().build();
    }
}
