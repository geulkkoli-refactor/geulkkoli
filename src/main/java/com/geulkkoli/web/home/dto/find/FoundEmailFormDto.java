package com.geulkkoli.web.home.dto.find;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class FoundEmailFormDto {

    @NotEmpty
    private final String email;

    public FoundEmailFormDto(String email) {
        this.email = email;
    }

}
