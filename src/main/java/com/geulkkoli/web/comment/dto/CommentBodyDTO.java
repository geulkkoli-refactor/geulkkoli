package com.geulkkoli.web.comment.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
public class CommentBodyDTO {

    @NotBlank
    @Size(min = 2, max = 200, message = "댓글은 2자 이상 200자 이하로 입력해주세요.")
    private String commentBody;

    @Builder
    public CommentBodyDTO(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentBody() {
        return commentBody;
    }


}
