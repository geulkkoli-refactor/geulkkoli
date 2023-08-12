package com.geulkkoli.web.follow.dto;

public class FollowResultDTO {
    private boolean mine;
    private boolean follow;

    public FollowResultDTO(boolean mine, boolean follow) {
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
