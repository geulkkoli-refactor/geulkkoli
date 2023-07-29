package com.geulkkoli.domain.posthashtag;

import com.geulkkoli.domain.post.Post;

import java.util.List;

public interface PostHashTagRepositoryCustom {

    List<Post> findAllByHashTagNames(List<String> hashTagNames);

}
