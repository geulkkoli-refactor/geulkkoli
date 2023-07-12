package com.geulkkoli.application.social.util;

import com.geulkkoli.application.user.ProviderUser;

import java.util.List;

public class DelegateOAuth2RequestConverter {
    private List<UserRequestConverter> userRequestConverters;

    public DelegateOAuth2RequestConverter() {
        this.userRequestConverters = List.of(new KaKaoRequestConverter(), new GoogleRequestConverter(), new NaverRequestConverter());
    }

    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        for (UserRequestConverter userRequestConverter : userRequestConverters) {
            if (userRequestConverter.supports(providerUserRequest.getClientRegistration().getRegistrationId())) {
                return userRequestConverter.convert(providerUserRequest);
            }
        }
        throw new IllegalArgumentException("지원하지 않는 OAuth2 공급자입니다.");
    }

}
