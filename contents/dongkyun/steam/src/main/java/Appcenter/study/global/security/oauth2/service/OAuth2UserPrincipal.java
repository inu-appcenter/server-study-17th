package Appcenter.study.global.security.oauth2.service;

import Appcenter.study.global.security.oauth2.user.OAuth2UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class OAuth2UserPrincipal implements OAuth2User, UserDetails {

    private final OAuth2UserInfo oAuth2UserInfo;

    public OAuth2UserPrincipal(OAuth2UserInfo oAuth2UserInfo) {
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    @Override
    public String getPassword() { return null; }

    @Override
    public String getUsername() { return oAuth2UserInfo.getEmail(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public Map<String, Object> getAttributes() { return oAuth2UserInfo.getAttributes(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.emptyList(); }

    @Override
    public String getName() { return oAuth2UserInfo.getName(); }

    public OAuth2UserInfo getUserInfo() { return oAuth2UserInfo; }
}
