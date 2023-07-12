package com.geulkkoli.web.admin;

import com.geulkkoli.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class ReportDto {

    @NotBlank
    private final Long postId;

    @NotBlank
    private final String title;

    @NotBlank
    private final String nickName;

    @NotBlank
    private final Long Reports;

    @Builder
    public ReportDto(Long postId, String title, String nickName, Long reports) {
        this.postId = postId;
        this.title = title;
        this.nickName = nickName;
        this.Reports = reports;
    }

    public static ReportDto toDto(Post post, Long reports) {
        return ReportDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .nickName(post.getNickName())
                .reports(reports)
                .build();
    }
}
