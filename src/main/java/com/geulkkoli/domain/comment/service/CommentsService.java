package com.geulkkoli.domain.comment.service;

import com.geulkkoli.domain.comment.Comments;
import com.geulkkoli.domain.comment.CommentsRepository;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.comment.dto.CommentEditDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    // 댓글 달기
    public Comments writeComment(CommentBodyDTO commentBody, Post post, User user) {
        Comments comments = user.writeComment(commentBody, post);
        return commentsRepository.save(comments);
    }

    // 댓글 수정하기
    public Comments editComment(CommentEditDTO commentEditDTO, User user) {
        Comments comments = commentsRepository.findById(commentEditDTO.getCommentId()).orElseThrow(() ->new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));
        user.editComment(comments, commentEditDTO);
        return commentsRepository.save(comments);
    }

    // 댓글 지우기
    public void deleteComment(Long commentId, User user) {
        Comments comments = commentsRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));
        commentsRepository.delete(user.deleteComment(comments));
    }
}
