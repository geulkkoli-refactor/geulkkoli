package com.geulkkoli.domain.follow.service;

import com.geulkkoli.domain.follow.Follow;
import com.geulkkoli.domain.follow.FollowRepository;
import com.geulkkoli.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final FollowRepository followRepository;


    public Follow follow(User followee, User follower) {
        if (follower.equals(followee)) {
            throw new CanNotFollowException("자기 자신을 팔로우할 수 없습니다.");
        }
        Follow follow = follower.follow(followee);
        return followRepository.save(follow);
    }

    public Follow unFollow(User followee, User follower) {
        Follow follow = followRepository.findByFolloweeUserIdAndFollowerUserId(followee.getUserId(), follower.getUserId()).orElseThrow(() -> new FollowNotFoundException("존재하지 않는 팔로우입니다."));
        followRepository.delete(follow);
        return follow;
    }

    public void allUnfollow(User deleteTarget) {
        followRepository.deleteAll(followRepository.findAllByFolloweeUserIdOrFollowerUserId(deleteTarget.getUserId(), deleteTarget.getUserId()));
    }
}
