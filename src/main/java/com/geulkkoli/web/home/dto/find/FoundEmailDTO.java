package com.geulkkoli.web.home.dto.find;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class FoundEmailDTO {


    @NotEmpty
    private final String email;

    public FoundEmailDTO(String email) {
        this.email = email;
    }

}
