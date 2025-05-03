package Appcenter.study.global.security.jwt;

import Appcenter.study.domain.member.Member;
import Appcenter.study.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 이메일 기반 사용자 조 ( 식별자 : 이메일 )
    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member =  memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // UserDetails 객체 생성
        return new UserDetailsImpl(member);
    }
}
