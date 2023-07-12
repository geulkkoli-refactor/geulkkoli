package com.geulkkoli.application.social.util;

import com.geulkkoli.application.user.ProviderUser;

public interface UserRequestConverter<T extends UserRequest, R extends ProviderUser> {

    R convert(T t);

    boolean supports(String registrationId);
}
