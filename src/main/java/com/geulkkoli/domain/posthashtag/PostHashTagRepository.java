package com.geulkkoli.domain.posthashtag;

import com.geulkkoli.domain.hashtag.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    List<PostHashTag> findAllByHashTag (HashTag hashTagId);
}
