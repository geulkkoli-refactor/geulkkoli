package com.geulkkoli.domain.post.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.util.HashTagSign;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostNotExistException;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.post.SearchType;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.web.blog.dto.ArticlePagingRequestDTO;
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

    public Page<ArticlePagingRequestDTO> searchPostsList(Pageable pageable, String searchType, String searchWord) {
        if (searchWord.contains(" ")) {
            List<String> searchWords = Arrays.stream(searchWord.split(" ")).map(String::trim).collect(Collectors.toList());
            if (SearchType.TITLE.getType().equals(searchType)) return searchPostsListByTitles(pageable, searchWords);
            if (SearchType.NICKNAME.getType().equals(searchType))
                return searchPostListByNickName(pageable, searchWords);
            if (SearchType.BODY.getType().equals(searchType)) return searchPostListByPostBodys(pageable, searchWords);
            if (SearchType.Multi_Hash_Tag.getType().equals(searchType))
                return searchPostsListByHashTags(pageable, searchWords);
        }

        if (SearchType.TITLE.getType().equals(searchType)) return searchPostsListByTitle(pageable, searchWord);
        if (SearchType.NICKNAME.getType().equals(searchType)) return searchPostListByNickName(pageable, searchWord);
        if (SearchType.BODY.getType().equals(searchType)) return searchPostListByPostBody(pageable, searchWord);
        if (SearchType.Multi_Hash_Tag.getType().equals(searchType))
            return searchPostsListByHashTag(pageable, searchWord);
        return postRepository.findAll(pageable)
                .map(ArticlePagingRequestDTO::toDTO);
    }

    private Page<ArticlePagingRequestDTO> searchPostsListByHashTag(Pageable pageable, String searchWords) {
        List<String> multiHashTagNames = Arrays.stream(searchWords.split(HashTagSign.GENERAL.getSign())).collect(Collectors.toUnmodifiableList());
        List<ArticlePagingRequestDTO> result = postRepository.allPostsMultiHashTags(multiHashTagNames).stream()
                .map(ArticlePagingRequestDTO::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(result, pageable, pageable.getPageSize());
    }
    private Page<ArticlePagingRequestDTO> searchPostsListByHashTags(Pageable pageable, List<String> multiHashTagNames) {
        List<String> multiHashTagNames2 = multiHashTagNames.stream().map(i -> i.replace(HashTagSign.GENERAL.getSign(), "")).collect(Collectors.toList());
        List<ArticlePagingRequestDTO> result = postRepository.allPostsMultiHashTags(multiHashTagNames2).stream()
                .map(ArticlePagingRequestDTO::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(result, pageable, pageable.getPageSize());
    }

    // 검색어가 여러개일 경우, 검색어를 공백으로 구분하여 검색하도록 수정
    private Page<ArticlePagingRequestDTO> searchPostsListByTitles(Pageable pageable, List<String> searchWords) {

        List<ArticlePagingRequestDTO> findPagesByTitle = searchWords.stream()
                .map(i -> postRepository.findPostsByTitleContaining(pageable, i))
                .map(i -> i.map(ArticlePagingRequestDTO::toDTO))
                .flatMap(Streamable::stream)
                .collect(Collectors.toList());

        return new PageImpl<>(findPagesByTitle, pageable, pageable.getPageSize());
    }

    private Page<ArticlePagingRequestDTO> searchPostsListByTitle(Pageable pageable, String searchWord) {
        return postRepository.findPostsByTitleContaining(pageable, searchWord)
                .map(ArticlePagingRequestDTO::toDTO);
    }

    // 검색어가 여러개일 경우, 검색어를 공백으로 구분하여 검색하도록 수정
    private Page<ArticlePagingRequestDTO> searchPostListByNickName(Pageable pageable, String nickName) {

        return postRepository.findPostsByNickNameContaining(pageable, nickName)
                .map(ArticlePagingRequestDTO::toDTO);
    }

    private Page<ArticlePagingRequestDTO> searchPostListByNickName(Pageable pageable, List<String> nickNames) {
        List<ArticlePagingRequestDTO> findPageByNickName = nickNames.stream()
                .map(i -> postRepository.findPostsByNickNameContaining(pageable, i))
                .map(i -> i.map(ArticlePagingRequestDTO::toDTO))
                .flatMap(Streamable::stream)
                .collect(Collectors.toList());

        return new PageImpl<>(findPageByNickName, pageable, pageable.getPageSize());
    }

    private Page<ArticlePagingRequestDTO> searchPostListByPostBody(Pageable pageable, String postBody) {

        return postRepository.findPostsByPostBodyContaining(pageable, postBody)
                .map(ArticlePagingRequestDTO::toDTO);
    }

    private Page<ArticlePagingRequestDTO> searchPostListByPostBodys(Pageable pageable, List<String> postBodys) {
        List<ArticlePagingRequestDTO> findPageByPostBody = postBodys.stream()
                .map(i -> postRepository.findPostsByPostBodyContaining(pageable, i))
                .map(i -> i.map(ArticlePagingRequestDTO::toDTO))
                .flatMap(Streamable::stream)
                .collect(Collectors.toList());

        return new PageImpl<>(findPageByPostBody, pageable, pageable.getPageSize());
    }

    public Page<ArticlePagingRequestDTO> findPostByTag(Pageable pageable, List<HashTag> hashTag) {
        List<String> hashTagNames = hashTag.stream().map(HashTag::getHashTagName).collect(Collectors.toList());
        List<ArticlePagingRequestDTO> articlePagingRequestDTOS = postRepository.allPostsMultiHashTags(pageable,hashTagNames)
                .stream().map(ArticlePagingRequestDTO::toDTO).collect(Collectors.toList());

        return new PageImpl<>(articlePagingRequestDTOS, pageable, pageable.getPageSize());
    }
}
