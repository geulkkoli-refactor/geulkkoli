package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagSign;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.web.post.dto.PostRequestListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostHahTagFindService {
    private final HashTagRepository hashTagRepository;
    private final PostHashTagRepository postHashTagRepository;

    public Page<PostRequestListDTO> searchPostsListByHashTag(Pageable pageable, String searchWords) {

        List<HashTag> tags = findHashTag(searchWords);
        List<Post> resultList = searchPostContainAllHashTags(tags);

        return convertPostSearchRequestListDTO(pageable, resultList).map(post -> new PostRequestListDTO(
                post.getPostId(),
                post.getTitle(),
                post.getNickName(),
                post.getUpdatedAt(),
                post.getPostHits()
        ));
    }

    //searchPostsListByHashTag의 페이징 처리를 위해, 페이징 값을 반환해줍니다.
    private Page<Post> convertPostSearchRequestListDTO(Pageable pageable, List<Post> resultList) {
        resultList.sort(Comparator.comparing(Post::getUpdatedAt).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());
        return new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
    }

    //웹에서 받은 문자열로 하여금 해시태그로 나눠줍니다.
    private List<HashTag> findHashTag(String searchWords) {
        return Arrays.stream(searchWords.split(HashTagSign.GENERAL.getSign()))
                .map(String::strip)
                .map(hashTagRepository::findByHashTagName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    //해당 태그를 가진 게시글을 찾아냅니다.
    private List<Post> searchPostContainAllHashTags(List<HashTag> tags) {

        if (tags.isEmpty()) {
            return postHashTagRepository.findAll().stream().map(PostHashTag::getPost).collect(Collectors.toList());
        }

        List<String> hashTagNames = tags.stream().map(HashTag::getHashTagName).collect(Collectors.toList());
        return postHashTagRepository.findAllByHashTagNames(hashTagNames);
    }
}
