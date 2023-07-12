package com.geulkkoli.web.follow.dto;

public class FollowResult {
    private boolean mine;
    private boolean follow;

    public FollowResult(boolean mine, boolean follow) {
        this.mine = mine;
        this.follow = follow;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isFollow() {
        return follow;
    }
}
