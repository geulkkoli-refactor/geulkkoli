package com.geulkkoli.domain.post.service;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostNotExistException;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFindService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("No post found id matches:" + postId));
    }

    @Transactional(readOnly = true)
    public List<String> getCreatedAts(User user) {
        return postRepository.findCreatedAt(user.getUserId());
    }
}
