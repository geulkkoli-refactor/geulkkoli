package com.geulkkoli.application.social.util;

import com.geulkkoli.application.user.ProviderUser;
import com.geulkkoli.application.social.NaverOAuth2User;

import java.util.Map;

public class NaverRequestConverter implements UserRequestConverter<ProviderUserRequest, ProviderUser>{
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        return new NaverOAuth2User(getAttributes(providerUserRequest), providerUserRequest.getOAuth2User(), providerUserRequest.getClientRegistration());
    }

    @Override
    public boolean supports(String registrationId) {
        return SocialType.NAVER.getValue().equals(registrationId);
    }

    private Map<String, Object> getAttributes(ProviderUserRequest providerUserRequest) {
        Map<String, Object> naverResponse = (Map<String, Object>) providerUserRequest.getOAuth2User().getAttributes().get("response");
        return naverResponse;
    }
}
