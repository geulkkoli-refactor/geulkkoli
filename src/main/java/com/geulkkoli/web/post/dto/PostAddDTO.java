package com.geulkkoli.web.post.dto;

import lombok.Builder;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Setter
public class PostAddDTO {

    @NotNull
    private Long authorId;

    @NotBlank
    @Length(min = 2, max = 100)
    private String title;

    @NotBlank
    @Length(min = 20, max = 10000)
    private String postBody;

    @NotBlank
    private String nickName;

    private String hashTagString = "";

    public PostAddDTO() {
    }


    @Builder
    public PostAddDTO(Long authorId, String title, String postBody
            , String nickName, String tagList) {
        this.authorId = authorId;
        this.title = title;
        this.postBody = HtmlUtils.htmlEscape(postBody);
        this.nickName = nickName;
        this.hashTagString = tagList;

    }

    public Long getAuthorId() {
        return authorId;
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

    public String getHashTagString() {
        return hashTagString;
    }

    public List<String> tagLists() {
        return Arrays.stream(hashTagString.split(" ")).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "AddDTO{" +
                "authorId=" + authorId +
                ", title='" + title + '\'' +
                ", postBody='" + postBody + '\'' +
                ", nickName='" + nickName + '\'' +
                ", tagList='" + hashTagString + '\'' +
                '}';
    }
}
