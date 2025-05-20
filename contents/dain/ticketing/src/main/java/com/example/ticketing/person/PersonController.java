package com.example.ticketing.person;

import com.example.ticketing.person.dto.req.PersonLoginRequestDto;
import com.example.ticketing.person.dto.req.PersonSignupRequestDto;
import com.example.ticketing.person.dto.req.PersonUpdateRequestDto;
import com.example.ticketing.person.dto.res.PersonGetResponseDto;
import com.example.ticketing.person.dto.res.PersonLoginResponseDto;
import com.example.ticketing.person.dto.res.PersonSignupResponseDto;
import com.example.ticketing.person.dto.res.PersonUpdateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class PersonController implements PersonApiDocs{

    private final PersonService personService;

    public ResponseEntity<PersonSignupResponseDto> signup(@Valid @RequestBody PersonSignupRequestDto requestDto) {
        return ResponseEntity.ok(personService.signup(requestDto));
    }

    public ResponseEntity<PersonLoginResponseDto> login(@Valid @RequestBody PersonLoginRequestDto loginRequest) {
        return ResponseEntity.ok(personService.login(loginRequest));
    }

    public ResponseEntity<PersonGetResponseDto> getPerson(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(personService.getPerson(user));
    }

    public ResponseEntity<List<PersonGetResponseDto>> getPersonList() {
        List<PersonGetResponseDto> responseList = personService.getPersonList();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    public ResponseEntity<PersonUpdateResponseDto> update(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody PersonUpdateRequestDto updateRequest) {
        return ResponseEntity.ok(personService.update(user, updateRequest));
    }

    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails user) {
        personService.delete(user);
        return ResponseEntity.noContent().build();
    }
}
