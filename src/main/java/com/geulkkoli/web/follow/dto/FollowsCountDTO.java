package com.geulkkoli.web.follow.dto;

public class FollowsCountDTO {
    private final Integer followeeCount;
    private final Integer followerCount;

    private FollowsCountDTO(Integer followeeCount, Integer followerCount) {
        this.followeeCount = followeeCount;
        this.followerCount = followerCount;
    }

    public static FollowsCountDTO of(Integer followeeCount, Integer followerCount) {
        return new FollowsCountDTO(followeeCount, followerCount);
    }

    public Integer getFolloweeCount() {
        return followeeCount;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }
}
