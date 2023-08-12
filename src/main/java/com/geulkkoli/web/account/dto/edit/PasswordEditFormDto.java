package com.geulkkoli.web.account.dto.edit;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class PasswordEditFormDto {

    @NotEmpty
    // 기존 비밀번호는 길이, 패턴이 설정된 상태라서 생략
    private String oldPassword;

    @NotEmpty
    @Length(min = 8, max = 20)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*\\W])(?=\\S+$).{8,20}")
    private String newPassword;

    @NotEmpty
    private String verifyPassword;

    public PasswordEditFormDto(String oldPassword, String newPassword, String verifyPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.verifyPassword = verifyPassword;
    }

    public boolean isMatched(){
        return newPassword.equals(verifyPassword);
    }
}
