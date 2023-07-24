package com.geulkkoli.domain.post.service;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostNotExistException;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.web.post.dto.PostRequestListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostFindService {
    private final PostRepository postRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("No post found id matches:" + postId));
    }


    public List<String> getCreatedAts(User user) {
        return postRepository.findCreatedAt(user.getUserId());
    }

    public Page<PostRequestListDTO> searchPostsListByTitle(Pageable pageable, String title) {
        if (title.contains(" ")) {
            List<PostRequestListDTO> collect = Arrays.stream(title.split(" "))
                    .map(String::trim)
                    .map(i -> postRepository.findPostsByTitleContaining(pageable, i))
                    .map(i -> i.map(PostRequestListDTO::toDTO))
                    .flatMap(Streamable::stream)
                    .collect(Collectors.toList());

            return new PageImpl<>(collect, pageable, pageable.getPageSize());
        }
        return postRepository.findPostsByTitleContaining(pageable, title)
                .map(PostRequestListDTO::toDTO);
    }

    public Page<PostRequestListDTO> searchPostListByNickName(Pageable pageable, String nickName) {
        if (nickName.contains(" ")) {
            List<PostRequestListDTO> collect = Arrays.stream(nickName.split(" "))
                    .map(String::trim)
                    .map(i -> postRepository.findPostsByNickNameContaining(pageable, i))
                    .map(i -> i.map(PostRequestListDTO::toDTO))
                    .flatMap(Streamable::stream)
                    .collect(Collectors.toList());

            return new PageImpl<>(collect, pageable, pageable.getPageSize());
        }


        return postRepository.findPostsByNickNameContaining(pageable, nickName)
                .map(PostRequestListDTO::toDTO);
    }

    public Page<PostRequestListDTO> searchPostListByPostBody(Pageable pageable, String postBody) {
        if (postBody.contains(" ")) {
            List<PostRequestListDTO> collect = Arrays.stream(postBody.split(" "))
                    .map(String::trim)
                    .map(i -> postRepository.findPostsByPostBodyContaining(pageable, i))
                    .map(i -> i.map(PostRequestListDTO::toDTO))
                    .flatMap(Streamable::stream)
                    .collect(Collectors.toList());

            return new PageImpl<>(collect, pageable, pageable.getPageSize());
        }

        return postRepository.findPostsByPostBodyContaining(pageable, postBody)
                .map(PostRequestListDTO::toDTO);
    }
}
