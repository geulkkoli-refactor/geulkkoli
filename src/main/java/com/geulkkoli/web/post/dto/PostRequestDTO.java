package com.geulkkoli.web.post.dto;

import com.geulkkoli.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostRequestDTO {

    private Long postId;

    private String title;

    private String nickName;

    private String contentSummary;

    private String date;

    private int postHits;

    @Builder
    public PostRequestDTO(Long postId, String title, String nickName, String contentSummary, String date, int postHits) {
        this.postId = postId;
        this.title = title;
        this.nickName = nickName;
        this.contentSummary = contentSummary.replaceAll("<[^>]*>", "").substring(0, 1);
        this.date = date;
        this.postHits = postHits;
    }

    public static PostRequestDTO toDTO(Post post) {
        return PostRequestDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .contentSummary(post.getPostBody())
                .nickName(post.getNickName())
                .date(String.valueOf(post.getUpdatedAt()))
                .postHits(post.getPostHits())
                .build();
    }

    @Override
    public String toString() {
        return "PostRequestListDTO{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", nickName='" + nickName + '\'' +
                ", date='" + date + '\'' +
                ", postHits=" + postHits +
                '}';
    }
}