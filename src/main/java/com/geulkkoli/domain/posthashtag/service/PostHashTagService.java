package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.hashtag.service.HashTagFindService;
import com.geulkkoli.domain.hashtag.service.HashTagService;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.web.blog.dto.WriteRequestDTO;
import com.geulkkoli.web.blog.dto.ArticleEditRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PostHashTagService {

    private final HashTagFindService hashTagFindService;
    private final PostHashTagRepository postHashTagRepository;
    private final HashTagService hashTagService;

    /**
     * 게시글에 해시태그를 추가하는 메서드
     *
     * @param post
     * @param WriteRequestDTO
     * @return Post
     * @see HashTagFindService#findHashTags(List)
     * @see HashTagService#createNewHashTag(String, String)
     * @see Post#addMultiHashTags(List)
     * @see PostHashTagRepository#saveAll(Iterable)
     */
    public Post addHashTagsToPost(Post post, WriteRequestDTO WriteRequestDTO) {
        if (WriteRequestDTO.getHashTagString().isEmpty()) {
            return post;
        }
        List<HashTag> hashTags = hashTagFindService.findHashTags(WriteRequestDTO.tagLists());
        log.info("hashTags: {}", hashTags);
        Optional<HashTag> any = hashTags.stream().filter(i -> WriteRequestDTO.getHashTagString().equals(i.getHashTagName())).findAny();
        if (any.isEmpty() && !WriteRequestDTO.getHashTagString().isEmpty()) {
            HashTag newHashTag = hashTagService.createNewHashTag(WriteRequestDTO.getHashTagString(), HashTagType.GENERAL.getTypeName());
            hashTags.add(newHashTag);
            postHashTagRepository.saveAll(post.addMultiHashTags(hashTags));
            return post;
        }
        postHashTagRepository.saveAll(post.addMultiHashTags(hashTags));
        return post;
    }

    /**
     * 게시글에 해시태그를 수정하는 메서드
     *
     * @param post
     * @param updateParam
     * @return Post
     * @see HashTagFindService#findHashTags(List)
     * @see HashTagService#createNewHashTag(String, String)
     */
    public Post editHashTagsToPost(Post post, ArticleEditRequestDTO updateParam) {
        log.info("updateParam: {}", updateParam.getTags());

        List<HashTag> hashTags = hashTagFindService.findHashTags(updateParam.tagNames());
        List<String> findHashTagNames = hashTags.stream().map(HashTag::getHashTagName).collect(Collectors.toList());
        List<String> newHashTagNames = updateParam.tagNames().stream().filter(name -> !findHashTagNames.contains(name)).collect(Collectors.toList());
        if (newHashTagNames.isEmpty()) {
            log.info("hashTags: {}", hashTags);
            postHashTagRepository.deleteAll(post.deleteAllPostHashTag());
            postHashTagRepository.saveAll(post.editMultiHashTags(hashTags));
            log.info("postHashTags: {}", post.getPostHashTags());
            return post;
        }
        newHashTagNames.forEach(tagName -> {
            HashTag newHashTag = hashTagService.createNewHashTag(tagName, HashTagType.GENERAL.getTypeName());
            hashTags.add(newHashTag);
        });

        log.info("hashTags: {}", hashTags);
        postHashTagRepository.deleteAll(post.deleteAllPostHashTag());
        postHashTagRepository.saveAll(post.editMultiHashTags(hashTags));
        return post;
    }
}