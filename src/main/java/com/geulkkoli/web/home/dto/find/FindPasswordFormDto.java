package com.geulkkoli.web.home.dto.find;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class FindPasswordFormDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String userName;

    @NotEmpty
    @Pattern(regexp = "^[*\\d]*$")
    private String phoneNo;

    public FindPasswordFormDto(String email, String userName, String phoneNo) {
        this.email = email;
        this.userName = userName;
        this.phoneNo = phoneNo;
    }
}
