package com.geulkkoli.web.follow.dto;

public class FollowsCount {
    private final Integer followeeCount;
    private final Integer followerCount;

    private FollowsCount(Integer followeeCount, Integer followerCount) {
        this.followeeCount = followeeCount;
        this.followerCount = followerCount;
    }

    public static FollowsCount of(Integer followeeCount, Integer followerCount) {
        return new FollowsCount(followeeCount, followerCount);
    }

    public Integer getFolloweeCount() {
        return followeeCount;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }
}
