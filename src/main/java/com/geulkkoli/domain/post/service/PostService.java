package com.geulkkoli.domain.post.service;

import com.geulkkoli.domain.post.NotAuthorException;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostNotExistException;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.posthashtag.service.PostHashTagService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserNotExistException;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.blog.dto.WriteRequestDTO;
import com.geulkkoli.web.blog.dto.ArticleEditRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
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

    //게시글 상세보기만을 담당하는 메서드
    public Post showDetailPost(Long postId) {
        postRepository.updateHits(postId);
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("No post found id matches:" + postId));
    }

    public Post writeArtice(WriteRequestDTO WriteRequestDTO, User user) {
        Post writePost = user.writePost(WriteRequestDTO);
        Post save = postRepository.save(writePost);
        postHashTagService.addHashTagsToPost(save, WriteRequestDTO);

        return save;
    }

    /**
     * @param post
     * @param updateParam
     * @return postHashTagService의 hashTagSerparator가 해시태그를 찾아 List<HashTag>로 반환한다. 나
     */
    public Post updatePost(Post post, ArticleEditRequestDTO updateParam) {
        post.getUser().editPost(post, updateParam);
        if (updateParam.getTags().isEmpty()) {
            post.deleteAllPostHashTag();
            return postRepository.save(post);
        }
       return postHashTagService.editHashTagsToPost(post, updateParam);
    }


    public void deletePost(Long postId, Long loggingUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    PostNotExistException postNotExistException = new PostNotExistException("해당 아이디를 가진 게시글이 존재하지 않습니다: " + postId);
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

    public void deletePost(Post post, User user) {
        user.deletePost(post);
        post.deleteAllPostHashTag();
        postRepository.delete(post);
    }

    public void deleteAll() {
        postRepository.deleteAll();
    }


}
