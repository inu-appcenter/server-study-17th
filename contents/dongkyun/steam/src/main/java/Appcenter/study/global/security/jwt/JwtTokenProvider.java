package Appcenter.study.global.security.jwt;

import Appcenter.study.domain.member.dto.res.LoginResponseDto;
import Appcenter.study.global.exception.CustomException;
import Appcenter.study.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    // JWT 클레임에서 권한 정보를 저장할 키
    private static final String AUTHORITIES_KEY = "auth";

    // 액세스 토큰 유효 시간 (2시간)
    private final long accessTokenValidityInSeconds;

    // 시크릿 키 및 종속성 ( Key : 알고리즘용 암호화 키 )
    private final String secret;
    private Key key;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret, UserDetailsServiceImpl userDetailsService) {
        this.secret = secret;

        this.accessTokenValidityInSeconds = 2 * 60 * 60 * 1000L;

        this.userDetailsService = userDetailsService;
    }

    // 인증 성공 시 토큰 생성
    public LoginResponseDto generateToken(Authentication authentication) {

        // 권한 정보를 문자열로 변환 ( ROLE_USER, ROLE_ADMIN,... )
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰 만료 시간 계산
        long now = new Date().getTime();
        Date accessTime = new Date(now + this.accessTokenValidityInSeconds);

        // JWT 토큰 빌더
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessTime)
                .compact();

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpires(this.accessTokenValidityInSeconds - 5000)
                .accessTokenExpiresDate(accessTime)
                .build();
    }

    // 토큰으로부터 인증 정보 조회
    public Authentication getAuthentication(String token) {

        // 토큰 복호화 및 클레임 추출
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 사용자 정보 조회
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        // 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // 서명 불일치
            throw new CustomException(ErrorCode.JWT_SIGNATURE);
        } catch (MalformedJwtException e){
            // 구조 문제
            throw new CustomException(ErrorCode.JWT_MALFORMED);
        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            throw new CustomException(ErrorCode.JWT_REFRESH_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 형식
            throw new CustomException(ErrorCode.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            // 잘못된 인자
            throw new CustomException(ErrorCode.JWT_NOT_VALID);
        }
    }

    // 빈 초기화 시 키 생성
    @Override
    public void afterPropertiesSet() throws Exception {

        // Base64 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        // HMAC-SHA 키 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
