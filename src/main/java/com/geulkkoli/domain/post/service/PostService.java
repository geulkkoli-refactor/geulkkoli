package com.geulkkoli.domain.post.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagSign;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.NotAuthorException;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostNotExistException;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import com.geulkkoli.domain.posthashtag.service.PostHashTagService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserNotExistException;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.EditDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostHashTagService postHashTagService;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostHashTagService postHashTagService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postHashTagService = postHashTagService;
    }

    @Transactional
    //게시글 상세보기만을 담당하는 메서드
    public Post showDetailPost(Long postId) {
        postRepository.updateHits(postId);
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("No post found id matches:" + postId));
    }

    @Transactional
    public Post savePost(AddDTO addDTO, User user) {
        Post writePost = user.writePost(addDTO);
        Post save = postRepository.save(writePost);

        List<HashTag> hashTags = postHashTagService.hashTagSeparator(HashTagSign.GENERAL + HashTagType.GENERAL.getTypeName() + addDTO.addHasTageSignToTageList() + addDTO.addHasTageSignToTagCategory() + addDTO.addHasTageSignToTagStatus());
        log.info("hashTags : " + hashTags);
        postHashTagService.validatePostHasType(hashTags);
        postHashTagService.addHashTagsToPost(save, hashTags);

        return save;
    }

    @Transactional
    public Post updatePost(Post post, EditDTO updateParam) {
        post.getUser().editPost(post, updateParam);
        ArrayList<PostHashTag> postHashTags = new ArrayList<>(post.getPostHashTags());
        if (updateParam.getTagListString() != null && !"".equals(updateParam.getTagListString())) {
            for (PostHashTag postHashTag : postHashTags) {
                post.deletePostHashTag(postHashTag);
            }
            List<HashTag> hashTags = postHashTagService.hashTagSeparator(HashTagSign.GENERAL.getSign() + HashTagType.GENERAL.getTypeName()
                    + updateParam.getTagListString() + updateParam.getTagCategory() + updateParam.getTagStatus());
            postHashTagService.validatePostHasType(hashTags);
            postHashTagService.addHashTagsToPost(post, hashTags);

        }
        return postRepository.save(post);
    }


    @Transactional
    public void deletePost(Long postId, Long loggingUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    PostNotExistException postNotExistException = new PostNotExistException("해당 아이디를 가진 게시글이 존재하지 않습니다: "+ postId);
                    log.error(postNotExistException.getMessage());
                    return postNotExistException;
                });
        User loggingUser = userRepository.findById(loggingUserId).orElseThrow(() -> {
            UserNotExistException userNotExistException = new UserNotExistException("일치하는 회원을 찾을 수 없습니다: " + loggingUserId);
            log.error(userNotExistException.getMessage());
            return userNotExistException;
        });

        if (!post.getUser().equals(loggingUser)) {
            NotAuthorException notAuthorException = new NotAuthorException("해당 게시글의 작성자가 아닙니다.");
            log.error(notAuthorException.getMessage());
            throw notAuthorException;
        }

        loggingUser.deletePost(post);
        post.deleteAllPostHashTag();
        postRepository.delete(post);
    }

    @Transactional
    public void deleteAll() {
        postRepository.deleteAll();
    }


}
