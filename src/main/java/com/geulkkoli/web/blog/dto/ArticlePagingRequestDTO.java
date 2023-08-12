package com.geulkkoli.web.blog.dto;

import com.geulkkoli.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.HtmlUtils;

@Getter
@Setter
public class ArticlePagingRequestDTO {

    private Long postId;

    private String title;

    private String nickName;

    private String contentSummary;

    private String date;

    private int postHits;


    @Builder
    public ArticlePagingRequestDTO(Long postId, String title, String nickName, String contentSummary, String date, int postHits) {
        this.postId = postId;
        this.title = title;
        this.nickName = nickName;
        this.contentSummary = HtmlUtils.htmlUnescape(contentSummary).replaceAll("<[^>]*>", "").substring(0, 1);
        this.date = date;
        this.postHits = postHits;
    }

    public static ArticlePagingRequestDTO toDTO(Post post) {
        return ArticlePagingRequestDTO.builder()
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
        return "PostRequestDTO{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", nickName='" + nickName + '\'' +
                ", contentSummary='" + contentSummary + '\'' +
                ", date='" + date + '\'' +
                ", postHits=" + postHits +
                '}';
    }
}
