package com.geulkkoli.domain.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long>, HashTagRepositoryCustom {
    HashTag findByHashTagName(String tagName);
}