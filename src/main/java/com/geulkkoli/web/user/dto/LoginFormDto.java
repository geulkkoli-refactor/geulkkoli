package com.geulkkoli.web.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class LoginFormDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
