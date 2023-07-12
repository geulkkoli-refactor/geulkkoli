package com.geulkkoli.web.social;

import com.geulkkoli.domain.social.service.SocialInfo;
import com.geulkkoli.domain.user.User;

public class SocialInfoDto {
    private String authorizationServerId;
    private String clientregistrationName;
    private User user;

    public static SocialInfoDto of(String authorizationServerId, String clientregistrationName, User user) {
        return new SocialInfoDto(authorizationServerId, clientregistrationName, user);
    }

    private SocialInfoDto(String authorizationServerId, String clientregistrationName, User user) {
        this.authorizationServerId = authorizationServerId;
        this.clientregistrationName = clientregistrationName;
        this.user = user;
    }

    public String getAuthorizationServerId() {
        return authorizationServerId;
    }

    public String getClientregistrationName() {
        return clientregistrationName;
    }

    public User getUser() {
        return user;
    }

    public SocialInfo toEntity() {
        return SocialInfo.builder()
                .socialId(authorizationServerId)
                .socialType(clientregistrationName)
                .user(user)
                .build();
    }
}
