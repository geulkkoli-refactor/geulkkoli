package com.geulkkoli.application.social;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoOAuth2User extends OAuth2ProviderUser{

    private final Map<String, Object> attributes;

    public KakaoOAuth2User(Map<String, Object> attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes, oAuth2User, clientRegistration);
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return getOAuth2User().getAttribute("id").toString();
    }

    @Override
    public String getUsername() {
        Map<String, Object> profile = (Map<String, Object>)attributes.get("profile");
        return profile.get("nickname").toString();
    }

    @Override
    public String getNickName() {
        Map<String, Object> profile = (Map<String, Object>)attributes.get("profile");
        return profile.get("nickname").toString() + "Kakao";    }


}
