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

    public Post addHashTagsToPost(Post post, AddDTO addDTO) {
        List<HashTag> hashTags = findHashTag(addDTO.getTagListString() + addDTO.getTagCategory() + addDTO.getTagStatus());
        for (HashTag tag : hashTags) {
            addHashTagToPost(post, tag);
        }
        return post;
    }

    public Post editHashTagsToPost(Post post, EditDTO updateParam) {
        findHashTag(updateParam.getTagListString() + updateParam.getTagCategory() + updateParam.getTagStatus()).forEach(tag -> addHashTagToPost(post, tag));
        return post;
    }

    public Post addHashTagsToPostNotice(Post post, AddDTO addDTO) {
        List<HashTag> hashTags = findHashTag(HashTagSign.GENERAL.getSign() + "공지글" + addDTO.getTagListString() + addDTO.getTagCategory() + addDTO.getTagStatus());
        for (HashTag tag : hashTags) {
            addHashTagToPost(post, tag);
        }
        return post;
    }

    public Post editHashTagsToPostNotice(Post post, EditDTO updateParam) {
        List<HashTag> hashTag = findHashTag(HashTagSign.GENERAL.getSign() + "공지글" + updateParam.getTagListString() + updateParam.getTagCategory() + updateParam.getTagStatus());

        List<PostHashTag> postHashTags = post.getPostHashTags();
        for (PostHashTag postHashTag : postHashTags) {
            postHashTagRepository.delete(postHashTag);
        }
        postHashTags.clear();
        for (HashTag tag : hashTag) {
            addHashTagToPost(post, tag);
        }

        return post;
    }

    //게시글에 해시태그 1개를 추가합니다
    private PostHashTag addHashTagToPost(Post post, HashTag tag) {
        return postHashTagRepository.save(post.addHashTag(tag));
    }
    //웹에서 받은 문자열로 하여금 해시태그로 나눠줍니다.
    private List<HashTag> findHashTag(String searchWords) {
        return Arrays.stream(searchWords.split(HashTagSign.GENERAL.getSign()))
                .map(String::strip)
                .map(hashTagRepository::findByHashTagName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<HashTag> hashTagCreator(String searchWords) {
        return Arrays.stream(searchWords.split(HashTagSign.GENERAL.getSign()))
                .map(String::strip)
                .map(hashTagRepository::findByHashTagName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}