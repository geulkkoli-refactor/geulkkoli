package com.geulkkoli.application.user;

import com.geulkkoli.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserModelDto {

    private String authorizaionServerId;
    private String userName;

    private String password;

    private String nickName;

    private String email;

    private String phoneNo;

    private String gender;

    @Builder
    public UserModelDto(String authorizaionServerId, String userName, String password, String nickName, String email, String phoneNo, String gender) {
        this.authorizaionServerId = authorizaionServerId;
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
    }


    public static UserModelDto toDto(User user) {
        return UserModelDto.builder()
                .authorizaionServerId(String.valueOf(user.getUserId()))
                .nickName(user.getNickName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNo(user.getPhoneNo())
                .userName(user.getUserName())
                .gender(user.getGender())
                .build();
    }

    public static UserModelDto toDto(User user, String authorizationServerId) {
        return UserModelDto.builder()
                .authorizaionServerId(authorizationServerId)
                .nickName(user.getNickName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNo(user.getPhoneNo())
                .userName(user.getUserName())
                .gender(user.getGender())
                .build();
    }
}
