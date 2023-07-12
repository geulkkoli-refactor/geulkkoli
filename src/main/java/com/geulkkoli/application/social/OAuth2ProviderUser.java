package com.geulkkoli.application.social;

import com.geulkkoli.application.user.ProviderUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public abstract class OAuth2ProviderUser implements ProviderUser {
    private Map<String, Object> attributes;
    private OAuth2User oAuth2User;
    private ClientRegistration clientRegistration;

    protected OAuth2ProviderUser(Map<String, Object> attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        this.attributes = attributes;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getPassword() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getEmail() {
        log.info("Email: {}", attributes.get("email"));
        return (String)attributes.get("email");
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId();
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities()
                .stream().map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority())
                ).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public OAuth2User getOAuth2User() {
        return oAuth2User;
    }

    @Override
    public String getGender() {
        Object gender = oAuth2User.getAttributes().get("gender");
        if (Objects.isNull(gender))
            return "None";
        return gender.toString();
    }

    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }
}
