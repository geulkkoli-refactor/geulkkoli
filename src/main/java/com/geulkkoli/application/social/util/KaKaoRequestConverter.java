package com.geulkkoli.application.social.util;

import com.geulkkoli.application.user.ProviderUser;
import com.geulkkoli.application.social.KakaoOAuth2User;

import java.util.Map;

public class KaKaoRequestConverter implements UserRequestConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        return new KakaoOAuth2User(getAttributes(providerUserRequest), providerUserRequest.getOAuth2User(), providerUserRequest.getClientRegistration());
    }

    @Override
    public boolean supports(String registrationId) {
        return SocialType.KAKAO.getValue().equals(registrationId);
    }

    private Map<String, Object> getAttributes(ProviderUserRequest providerUserRequest) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) providerUserRequest.getOAuth2User().getAttributes().get("kakao_account");
        return kakaoAccount;
    }
}
