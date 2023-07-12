package com.geulkkoli.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findAll(Pageable pageable);

    @Modifying
    @Query("update Post p set p.postHits = p.postHits+1 where p.postId=:postId")
    void updateHits(@Param("postId") Long postId);

    Page<Post> findPostsByTitleContaining(Pageable pageable, String searchWords);

    Page<Post> findPostsByNickNameContaining(Pageable pageable, String searchWords);

    Page<Post> findPostsByPostBodyContaining(Pageable pageable, String searchWords);
    @Query("select p.createdAt from Post p where p.user.userId=:userId")
    List<String> findCreatedAt(@Param("userId") Long userId);
}
