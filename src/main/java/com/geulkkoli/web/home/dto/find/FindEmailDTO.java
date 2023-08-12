package com.geulkkoli.web.home.dto.find;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class FindEmailDTO {

    @NotEmpty
    private String userName;

    @NotEmpty
    @Pattern(regexp = "^[*\\d]*$")
    private String phoneNo;

    public FindEmailDTO(String userName, String phoneNo) {
        this.userName = userName;
        this.phoneNo = phoneNo;
    }

}
