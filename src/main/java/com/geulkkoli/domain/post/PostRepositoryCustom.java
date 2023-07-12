package com.geulkkoli.domain.post;

import java.util.List;

public interface PostRepositoryCustom {

    void update(Long postId, Post updateParam);

    List<Post> postsByHashTag(String searchWords);


    List<Post> allPostsMultiHashTags(List<String> hashTagNames);

    List<Post> allPostsTitleAndMultiPosts(String title, List<String> hashTagNames);
}
