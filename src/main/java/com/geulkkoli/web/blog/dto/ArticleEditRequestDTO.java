package com.geulkkoli.web.blog.dto;

import com.geulkkoli.domain.hashtag.util.HashTagSign;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ArticleEditRequestDTO {

    @NotNull
    private Long postId;


    @NotBlank
    @Length(min = 2, max = 100)
    private String title;

    @Setter
    @NotBlank
    @Length(min = 10, max = 10000)
    private String postBody;


    private String tags = "";

    private final String nickName;


    @Builder
    public ArticleEditRequestDTO(Long postId, String title, String postBody,
                                 String nickName, String tags) {
        this.postId = postId;
        this.title = title;
        this.postBody = postBody;
        this.nickName = nickName;
        this.tags = tags;
    }

    public static ArticleEditRequestDTO toDTO(Post post) {
        List<PostHashTag> postHashTags = post.getPostHashTags();
        String tagNames = postHashTags.stream().map(i -> i.getHashTag().getHashTagName()).collect(Collectors.joining(HashTagSign.GENERAL.getSign()));
        return ArticleEditRequestDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .postBody(post.getPostBody())
                .nickName(post.getNickName())
                .tags(tagNames)
                .build();
    }
    public List<String> tagNames() {
        return Arrays.stream(tags.split(HashTagSign.GENERAL.getSign())).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "EditDTO{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", postBody='" + postBody + '\'' +
                ", tags='" + tags + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
