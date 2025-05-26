package com.example.ticketing.security;

import com.example.ticketing.exception.CustomException;
import com.example.ticketing.exception.ErrorCode;
import com.example.ticketing.person.Person;
import com.example.ticketing.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId){
        Person person = personRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_FOUND));
        // CustomUserDetails로 감싸서 반환
        return new CustomUserDetails(person);
    }
}
