package com.geulkkoli.domain.post;

import com.querydsl.core.Tuple;

import java.util.List;

public interface PostRepositoryCustom {

    void update(Long postId, Post updateParam);

    List<Post> postsByHashTag(String searchWords);

    List<Tuple> allPostsMultiHashTagsWithTuple(String hashTagName, String hashTagName2);
    List<Post> allPostsMultiHashTags(List<String> hashTagNames);

    List<Post> allPostsTitleAndMultiPosts(String title, List<String> hashTagNames);
}
