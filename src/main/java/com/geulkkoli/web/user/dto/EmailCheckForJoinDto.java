package com.geulkkoli.web.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class EmailCheckForJoinDto {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, max = 6)
    private String authenticationNumber;

}
