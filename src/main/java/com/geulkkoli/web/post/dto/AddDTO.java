package com.geulkkoli.web.post.dto;

import com.geulkkoli.domain.hashtag.HashTagSign;
import lombok.Builder;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Setter
public class AddDTO {

    @NotNull
    private Long authorId;

    @NotBlank
    @Length(min = 1, max = 100)
    private String title;

    @NotBlank
    @Length(min = 10, max = 10000)
    private String postBody;

    @NotBlank
    private String nickName;

    private String tagListString = "";

    private String tagCategory;

    private String tagStatus;

    public AddDTO() {
    }

    @Builder
    public AddDTO(Long authorId, String title, String postBody
            , String nickName, String tagListString, String tagCategory, String tagStatus) {
        this.authorId = authorId;
        this.title = title;
        this.postBody = postBody;
        this.nickName = nickName;
        this.tagListString = tagListString;
        this.tagCategory = tagCategory;
        this.tagStatus = tagStatus;
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

    public String getTagListString() {
        return tagListString;
    }

    public String getTagCategory() {
        return tagCategory;
    }

    public String getTagStatus() {
        return tagStatus;
    }

    public String tageCateGory() {
        return String.join("", tagCategory.split(HashTagSign.GENERAL.getSign()));
    }

    public String tagListString() {
        return String.join("", tagListString.split(HashTagSign.GENERAL.getSign()));
    }

    public String tagStatus() {
        return
                String.join("", tagStatus.split(HashTagSign.GENERAL.getSign()));
    }

    @Override
    public String toString() {
        return "AddDTO{" +
                "authorId=" + authorId +
                ", title='" + title + '\'' +
                ", postBody='" + postBody + '\'' +
                ", nickName='" + nickName + '\'' +
                ", tagListString='" + tagListString + '\'' +
                ", tagCategory='" + tagCategory + '\'' +
                ", tagStatus='" + tagStatus + '\'' +
                '}';
    }
}
