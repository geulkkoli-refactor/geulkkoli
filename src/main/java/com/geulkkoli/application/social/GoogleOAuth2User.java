package com.geulkkoli.application.social;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class GoogleOAuth2User extends OAuth2ProviderUser {
    private final Map<String, Object> attributes;
    public GoogleOAuth2User(Map<String, Object> attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes, oAuth2User, clientRegistration);
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getUsername() {
       return attributes.get("name").toString();
    }

    @Override
    public String getNickName() {
        return attributes.get("name").toString() + " " + attributes.get("family_name").toString();
    }
}
