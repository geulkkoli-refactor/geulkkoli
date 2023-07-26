package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagSign;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.EditDTO;
import com.geulkkoli.web.post.dto.PostRequestListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostHashTagService {

    private final HashTagRepository hashTagRepository;
    private final PostHashTagRepository postHashTagRepository;

    //게시글에 해시태그 1개를 추가합니다
    private PostHashTag addHashTagToPost(Post post, HashTag tag) {
        return postHashTagRepository.save(post.addHashTag(tag));
    }


    //게시글에 다수의 해시태그를 추가합니다
    public Post addHashTagsToPost(Post post, List<HashTag> tags) {
        for (HashTag tag : tags) {
            addHashTagToPost(post, tag);
        }
        return post;
    }

    public Post addHashTagsToPost(Post post, AddDTO addDTO) {
        List<HashTag> hashTags = hashTagSeparator(addDTO.getTagListString() + addDTO.getTagCategory() + addDTO.getTagStatus());
        for (HashTag tag : hashTags) {
            addHashTagToPost(post, tag);
        }
        return post;
    }

    public Post addHashTagsToPost(Post post, EditDTO updateParam) {
        hashTagSeparator(HashTagSign.GENERAL.getSign() + updateParam.getTagListString() + HashTagSign.GENERAL.getSign() + updateParam.getTagCategory() + HashTagSign.GENERAL.getSign() + updateParam.getTagStatus()).forEach(tag -> addHashTagToPost(post, tag));
        return post;
    }

    public Post addHashTagsToPostNotice(Post post, AddDTO addDTO) {
        List<HashTag> hashTags = hashTagSeparator(HashTagSign.GENERAL.getSign() + "공지글" + addDTO.getTagListString() + addDTO.getTagCategory() + addDTO.getTagStatus());
        for (HashTag tag : hashTags) {
            addHashTagToPost(post, tag);
        }
        return post;
    }

    public Post addHashTagsToPostNotice(Post post, EditDTO updateParam) {
        List<HashTag> hashTags = hashTagSeparator(HashTagSign.GENERAL.getSign() + "공지글" + HashTagSign.GENERAL.getSign() + HashTagSign.GENERAL.getSign() + updateParam.getTagListString() + HashTagSign.GENERAL.getSign() + updateParam.getTagCategory() + HashTagSign.GENERAL.getSign() + updateParam.getTagStatus());
        for (HashTag tag : hashTags) {
            addHashTagToPost(post, tag);
        }
        return post;
    }

    //게시판을 들어갔을 때, 게시글을 검색할 때 등 게시글을 가져오는 모든 경우에 쓰입니다.
    public Page<PostRequestListDTO> searchPostsListByHashTag(Pageable pageable, String searchWords) {

        List<HashTag> tags = hashTagSeparator(searchWords);
        List<Post> resultList = searchPostContainAllHashTags(tags);

        return getPosts(pageable, resultList).map(post -> new PostRequestListDTO(
                post.getPostId(),
                post.getTitle(),
                post.getNickName(),
                post.getUpdatedAt(),
                post.getPostHits()
        ));
    }

    //searchPostsListByHashTag의 페이징 처리를 위해, 페이징 값을 반환해줍니다.
    private Page<Post> getPosts(Pageable pageable, List<Post> resultList) {
        resultList.sort(Comparator.comparing(Post::getUpdatedAt).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());
        return new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
    }

    //웹에서 받은 문자열로 하여금 해시태그로 나눠줍니다.
    public List<HashTag> hashTagSeparator(String searchWords) {
        return Arrays.stream(searchWords.split(HashTagSign.GENERAL.getSign()))
                .map(String::strip)
                .map(hashTagRepository::findByHashTagName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    //해당 태그를 가진 게시글을 찾아냅니다.
    public List<Post> searchPostContainAllHashTags(List<HashTag> tags) {

        if (tags.isEmpty()) {
            return postHashTagRepository.findAll().stream().map(PostHashTag::getPost).collect(Collectors.toList());
        }

        List<String> hashTagNames = tags.stream().map(HashTag::getHashTagName).collect(Collectors.toList());
        return postHashTagRepository.findAllByHashTagNames(hashTagNames);
    }


    //웹에서 분류나 상태값을 받아오지 못하거나 관리 태그에 접근하려 하는 경우, 이를 막습니다.


}