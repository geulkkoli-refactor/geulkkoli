package com.geulkkoli.web.post.dto;

import com.geulkkoli.domain.hashtag.util.HashTagSign;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EditDTO {

    @NotNull
    private Long postId;


    @NotBlank
    @Length(min = 1, max = 100)
    private String title;

    @Setter
    @NotBlank
    @Length(min = 10, max = 10000)
    private String postBody;

    @NotBlank
    private String tagListString;

    private final String nickName;

    @NotBlank
    private final String tagCategory;

    @NotBlank
    private final String tagStatus;

    @Builder
    public EditDTO(Long postId, String title, String postBody,
                   String nickName, String tagListString, String tagCategory, String tagStatus) {
        this.postId = postId;
        this.title = title;
        this.postBody = postBody;
        this.nickName = nickName;
        this.tagListString = tagListString;
        this.tagCategory = tagCategory;
        this.tagStatus = tagStatus;
    }

    public static EditDTO toDTO(Post post) {
        List<PostHashTag> postHashTags = new ArrayList<>(post.getPostHashTags());
        String tagStatus = "";
        String tagCategory = "";

        if (postHashTags.get(0).getHashTag().getHashTagName().equals("공지글")) {
            postHashTags.remove(0);
        } else {
            postHashTags.remove(0);
            tagStatus = postHashTags.get((postHashTags.size() - 1)).getHashTag().getHashTagName();
            postHashTags.remove(postHashTags.size() - 1);
            tagCategory = postHashTags.get((postHashTags.size() - 1)).getHashTag().getHashTagName();
            postHashTags.remove(postHashTags.size() - 1);
        }

        String tags = "";
        for (PostHashTag name : postHashTags) {
            tags += " #" + name.getHashTag().getHashTagName();
        }
        return EditDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .postBody(post.getPostBody())
                .nickName(post.getNickName())
                .tagListString(tags)
                .tagCategory("#" + tagCategory)
                .tagStatus("#" + tagStatus)
                .build();
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .postBody(postBody)
                .build();
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
        return String.join("", tagStatus.split(HashTagSign.GENERAL.getSign()));
    }
}
