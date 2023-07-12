package com.geulkkoli.web.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentEditDTO {

    @NotNull
    private Long commentId;

    @NotBlank
    @Size(min = 2, max = 200, message = "댓글은 2자 이상 200자 이하로 입력해주세요.")
    private String commentBody;

    public CommentEditDTO() {}

    @Builder
    public CommentEditDTO(Long commentId, String commentBody) {
        this.commentId = commentId;
        this.commentBody = commentBody;
    }

}
