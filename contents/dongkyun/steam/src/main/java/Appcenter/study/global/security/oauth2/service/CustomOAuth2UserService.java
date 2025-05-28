package Appcenter.study.global.security.oauth2.service;

import Appcenter.study.domain.member.MemberRepository;
import Appcenter.study.domain.member.dto.res.LoginResponseDto;
import Appcenter.study.global.security.jwt.JwtTokenProvider;
import Appcenter.study.global.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import Appcenter.study.global.security.oauth2.user.OAuth2UserInfo;
import Appcenter.study.global.security.oauth2.user.OAuth2UserInfoFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
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
            log.debug("OAuth2 user loaded from provider: {}", userRequest.getClientRegistration().getRegistrationId());
            return convertToOAuth2Principal(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            log.warn("AuthenticationException during OAuth2 login: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected exception during OAuth2 login", e);
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User convertToOAuth2Principal(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        log.debug("Converting OAuth2User to OAuth2UserPrincipal - provider: {}, accessToken: {}", registrationId, accessToken);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes(), accessToken);

        log.debug("Extracted user info: email={}, name={}, id={}",
                oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getId());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            log.warn("Email not found in OAuth2 user info");
            throw new OAuth2AuthenticationProcessingException("Email Not Found With OAuth2UserInfo");
        }

        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }

    public Boolean checkUserPresent(String email) { return memberRepository.existsByEmail(email); }

    @Transactional
    public LoginResponseDto oAuth2Login(Authentication authentication) {
        log.debug("Generating JWT token for authentication: {}", authentication.getName());
        return jwtTokenProvider.generateToken(authentication);
    }
}
