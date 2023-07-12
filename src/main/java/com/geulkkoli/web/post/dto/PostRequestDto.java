package com.geulkkoli.web.post.dto;

import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class PostRequestDto {

    @NotEmpty
    private String title;

    @NotEmpty
    private String postBody;

    @NotEmpty
    private String nickName;


    public PostRequestDto(String title, String postBody, String nickName) {
        this.title = title;
        this.postBody = postBody;
        this.nickName = nickName;
    }


    public String getTitle() {
        return title;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getNickName() {
        return nickName;
    }
}
