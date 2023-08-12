package com.geulkkoli.web.home.dto.find;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class FindPasswordDTO {

    @NotEmpty
    private String email;

    @NotEmpty
    private String userName;

    @NotEmpty
    @Pattern(regexp = "^[*\\d]*$")
    private String phoneNo;

    public FindPasswordDTO(String email, String userName, String phoneNo) {
        this.email = email;
        this.userName = userName;
        this.phoneNo = phoneNo;
    }
}
