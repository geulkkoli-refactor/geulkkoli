package com.geulkkoli.web.post.dto;

import com.geulkkoli.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostRequestListDTO {

    @NotBlank
    private Long postId;

    @NotBlank
    private String title;

    @NotBlank
    private String nickName;

    @NotBlank
    private String date;

    @NotBlank
    private int postHits;

    @Builder
    public PostRequestListDTO(Long postId, String title, String nickName, String date, int postHits) {
        this.postId = postId;
        this.title = title;
        this.nickName = nickName;
        this.date = date;
        this.postHits = postHits;

    }

    public static PostRequestListDTO toDTO (Post post) {
        return PostRequestListDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .nickName(post.getNickName())
                .date(String.valueOf(post.getUpdatedAt()))
                .postHits(post.getPostHits())
                .build();
    }
}
