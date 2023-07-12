package com.geulkkoli.web.post;

import com.geulkkoli.domain.user.User;

public class UserProfileDTO {
    private final String nickName;
    private final Long userId;

    public UserProfileDTO(String nickName, Long userId) {
        this.nickName = nickName;
        this.userId = userId;
    }


    public static UserProfileDTO toDTO(User authorUser) {
        return new UserProfileDTO(authorUser.getNickName(), authorUser.getUserId());
    }

    public String getNickName() {
        return nickName;
    }

    public Long getUserId() {
        return userId;
    }
}
