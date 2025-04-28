package Appcenter.study.global.security.jwt;

import Appcenter.study.domain.member.Member;
import Appcenter.study.domain.member.dto.res.LoginResponseDto;
import Appcenter.study.global.exception.CustomException;
import Appcenter.study.global.exception.ErrorCode;
import Appcenter.study.domain.member.MemberRepository;
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

    private static final String AUTHORITIES_KEY = "auth";
    private final long accessTokenValidityInSeconds;
    private final String secret;
    private final UserDetailsServiceImpl userDetailsService;
    private Key key;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret, UserDetailsServiceImpl userDetailsService) {
        this.secret = secret;

        this.accessTokenValidityInSeconds = 2 * 60 * 60 * 1000L;

        this.userDetailsService = userDetailsService;
    }

    public LoginResponseDto generateToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date accessTime = new Date(now + this.accessTokenValidityInSeconds);

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

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(claims.getSubject());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            throw new CustomException(ErrorCode.JWT_SIGNATURE);
        } catch (MalformedJwtException e){
            throw new CustomException(ErrorCode.JWT_MALFORMED);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_REFRESH_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorCode.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.JWT_NOT_VALID);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
