package com.geulkkoli.web.home.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class LoginDTO {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
