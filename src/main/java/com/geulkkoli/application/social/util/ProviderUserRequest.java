package com.geulkkoli.application.social.util;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class ProviderUserRequest implements UserRequest {
    private final ClientRegistration clientRegistration;
    private final OAuth2User oAuth2User;

    public ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        this.clientRegistration = clientRegistration;
        this.oAuth2User = oAuth2User;
    }


    public OAuth2User getOAuth2User() {
        return oAuth2User;
    }



    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }
}
