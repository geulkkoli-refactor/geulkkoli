package com.geulkkoli.web.user.dto.edit;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@ToString
public class UserInfoEditFormDto {

    @NotEmpty
    private String userName;

    @NotEmpty
    @Length(min = 2, max = 8)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$")
    private String nickName;

    @NotEmpty
    @Length(min = 10, max = 11)
    @Pattern(regexp = "^[*\\d]*$")
    private String phoneNo;

    @NotEmpty
    private String gender;

    private UserInfoEditFormDto(String userName, String nickName, String phoneNo, String gender) {
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNo = phoneNo;
        this.gender = gender;
    }

    public static UserInfoEditFormDto form(String userName, String nickName, String phoneNo, String gender) {
      return new UserInfoEditFormDto(userName, nickName, phoneNo, gender);
    }
}
