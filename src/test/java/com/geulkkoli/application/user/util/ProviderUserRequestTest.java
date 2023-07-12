package com.geulkkoli.application.user.util;


import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.social.util.ProviderUserRequest;
import com.geulkkoli.application.social.util.SocialType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ProviderUserRequestTest {

    @DisplayName("프로바이더로부터 유저정보를 받아온다.")
    @Test
    public void create() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(SocialType.KAKAO.getValue())
                .authorizationGrantType(new AuthorizationGrantType(Role.USER.getRoleName()))
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .build();

        OAuth2User oAuth2User = new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        };

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oAuth2User);


        assertThat(providerUserRequest.getClientRegistration().getRegistrationId()).isEqualTo(SocialType.KAKAO.getValue());
    }
}
