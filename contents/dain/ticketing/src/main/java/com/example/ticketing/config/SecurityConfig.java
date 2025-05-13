package com.example.ticketing.config;

import com.example.ticketing.security.jwt.JwtFilter;
import com.example.ticketing.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //단방향 해시 알고리즘(복호화 불가)
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화(현재는 API서버라서)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 비활성화
                .cors(AbstractHttpConfigurer::disable)

                // 세션을 사용 x -> STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 회원가입, 로그인 외에는 인증 후에만 호출 가능
                // shows 생성, 수정, 삭제는 ADMIN만 허용
                // tickets 생성, 수정, 삭제는 ADMIN만 허용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/persons/signup", "/api/persons/login", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/shows").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/shows/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/shows/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/tickets").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/tickets/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/tickets/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // JWT 검증 필터 삽입
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
