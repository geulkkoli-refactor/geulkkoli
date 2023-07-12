package com.geulkkoli.application.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProviderUser {
    String getId();
    String getUsername();
    String getPassword();
    String getEmail();

    String getNickName();

    String getGender();
    String getProvider();
    Collection<GrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();
    OAuth2User getOAuth2User();
}
