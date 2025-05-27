package Appcenter.study.global.security.oauth2.service;

import Appcenter.study.domain.member.MemberRepository;
import Appcenter.study.domain.member.dto.res.LoginResponseDto;
import Appcenter.study.global.security.jwt.JwtTokenProvider;
import Appcenter.study.global.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import Appcenter.study.global.security.oauth2.user.OAuth2UserInfo;
import Appcenter.study.global.security.oauth2.user.OAuth2UserInfoFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return convertToOAuth2Principal(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User convertToOAuth2Principal(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes(), accessToken);

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail()))
            throw new OAuth2AuthenticationProcessingException("Email Not Found With OAuth2UserInfo");

        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }

    public Boolean checkUserPresent(String email) { return memberRepository.existsByEmail(email); }

    @Transactional
    public LoginResponseDto oAuth2Login(Authentication authentication) {
        return jwtTokenProvider.generateToken(authentication);
    }
}
