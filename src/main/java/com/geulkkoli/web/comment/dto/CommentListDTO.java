package com.geulkkoli.web.comment.dto;

import com.geulkkoli.domain.comment.Comments;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CommentListDTO {

    @NotBlank
    private Long commentId;

    @NotBlank
    private String nickName;

    @NotBlank
    private String commentBody;

    @NotBlank
    private String date;

    @Builder
    public CommentListDTO(Long commentId, String nickName, String commentBody, String date) {
        this.commentId = commentId;
        this.nickName = nickName;
        this.commentBody = commentBody;
        this.date = date;
    }

    public static CommentListDTO toDTO (Comments comment) {
        return CommentListDTO.builder()
                .commentId(comment.getCommentId())
                .commentBody(comment.getCommentBody())
                .nickName(comment.getUser().getNickName())
                .date(String.valueOf(comment.getUpdatedAt()))
                .build();
    }
}
