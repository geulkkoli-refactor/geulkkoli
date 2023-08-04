package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.hashtag.service.HashTagFindService;
import com.geulkkoli.domain.hashtag.service.HashTagService;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.EditDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PostHashTagService {

    private final HashTagFindService hashTagFindService;
    private final PostHashTagRepository postHashTagRepository;
    private final HashTagService hashTagService;

    public Post addHashTagsToPost(Post post, AddDTO addDTO) {
        if (addDTO.getTagListString().isEmpty()) {
            List<HashTag> hashTags = hashTagFindService.findHashTags(addDTO.tagListString(), addDTO.tageCateGory(), addDTO.tagStatus());
            postHashTagRepository.saveAll(post.addMultiHashTags(hashTags));
            return post;
        }

        List<HashTag> hashTags = hashTagFindService.findHashTags(addDTO.tagListString(), addDTO.tageCateGory(), addDTO.tagStatus());
        log.info("hashTags: {}", hashTags);
        Optional<HashTag> any = hashTags.stream().filter(i -> addDTO.getTagListString().equals(i.getHashTagName())).findAny();
        if (any.isEmpty() && !addDTO.getTagListString().isEmpty()) {
            HashTag newHashTag = hashTagService.createNewHashTag(addDTO.getTagListString(), HashTagType.GENERAL.getTypeName());
            hashTags.add(newHashTag);
            postHashTagRepository.saveAll(post.addMultiHashTags(hashTags));
            return post;
        }
        postHashTagRepository.saveAll(post.addMultiHashTags(hashTags));
        return post;
    }

    public Post editHashTagsToPost(Post post, EditDTO updateParam) {
        List<HashTag> hashTags = hashTagFindService.findHashTags(updateParam.tagListString(), updateParam.tageCateGory(), updateParam.tagStatus());
        Optional<HashTag> any = hashTags.stream().filter(i -> updateParam.getTagListString().equals(i.getHashTagName())).findAny();
        if (any.isEmpty() &&!updateParam.getTagListString().isEmpty()) {
            HashTag newHashTag = hashTagService.createNewHashTag(updateParam.getTagListString(), HashTagType.GENERAL.getTypeName());
            hashTags.add(newHashTag);
            postHashTagRepository.saveAll(post.editMultiHashTags(hashTags));
            return post;
        }
        postHashTagRepository.saveAll(post.editMultiHashTags(hashTags));
        return post;
    }

    public Post addHashTagsToPostNotice(Post post, AddDTO addDTO) {
        List<HashTag> hashTags = hashTagFindService.findHashTags(addDTO.getTagListString(), addDTO.getTagCategory(), addDTO.getTagStatus());
        Optional<HashTag> any = hashTags.stream().filter(i -> addDTO.getTagListString().equals(i.getHashTagName())).findAny();
        if (any.isEmpty()) {
            HashTag newHashTag = hashTagService.createNewHashTag(addDTO.getTagListString(), HashTagType.GENERAL.getTypeName());
            hashTags.add(newHashTag);
            postHashTagRepository.saveAll(post.addMultiHashTags(hashTags));
            return post;
        }

        postHashTagRepository.saveAll(post.addMultiHashTags(hashTags));
        return post;
    }

    public Post editHashTagsToPostNotice(Post post, EditDTO updateParam) {
        List<HashTag> hashTags = hashTagFindService.findHashTags(updateParam.getTagListString(), updateParam.getTagCategory(), updateParam.getTagStatus());
        Optional<HashTag> any = hashTags.stream().filter(i -> updateParam.getTagListString().equals(i.getHashTagName())).findAny();
        if (any.isEmpty()) {
            HashTag newHashTag = hashTagService.createNewHashTag(updateParam.getTagListString(), HashTagType.GENERAL.getTypeName());
            hashTags.add(newHashTag);
            postHashTagRepository.saveAll(post.editMultiHashTags(hashTags));
            return post;
        }
        postHashTagRepository.saveAll(post.editMultiHashTags(hashTags));
        return post;
    }

}