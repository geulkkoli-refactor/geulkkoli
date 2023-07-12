package com.geulkkoli.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom{

    Integer countByFolloweeUserId(Long followeeId);

    Integer countByFollowerUserId(Long followerId);

    List<Follow> findAllByFolloweeUserIdOrFollowerUserId(Long followeeId, Long followerId);

    Optional<Follow> findByFolloweeUserIdAndFollowerUserId(Long followeeId, Long followerId);

    Boolean existsByFolloweeUserIdAndFollowerUserId(Long followeeId, Long followerId);
}
