package com.geulkkoli.application.social.util;

import com.geulkkoli.application.user.ProviderUser;
import com.geulkkoli.application.social.GoogleOAuth2User;

public class GoogleRequestConverter implements UserRequestConverter<ProviderUserRequest, ProviderUser>{
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        return new GoogleOAuth2User(providerUserRequest.getOAuth2User().getAttributes(), providerUserRequest.getOAuth2User(), providerUserRequest.getClientRegistration());
    }

    @Override
    public boolean supports(String registrationId) {
        return SocialType.GOOGLE.getValue().equals(registrationId);
    }
}
