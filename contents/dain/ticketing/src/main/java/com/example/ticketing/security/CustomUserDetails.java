package com.example.ticketing.security;

import com.example.ticketing.person.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Person person;

    public CustomUserDetails(Person person) {
        this.person = person;
    }

    @Override
    public String getUsername() {
        return person.getLoginId();
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Person이 갖고 있는 Role enum으로 권한 매핑
        return List.of(new SimpleGrantedAuthority(person.getRole().name()));
    }

    // 나머지 메서드는 모두 true 처리
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
}
