package com.geulkkoli.web.home.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class EmailCheckForJoinDTO {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, max = 6)
    private String authenticationNumber;

}
