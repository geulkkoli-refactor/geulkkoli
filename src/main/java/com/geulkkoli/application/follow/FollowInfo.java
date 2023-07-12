package com.geulkkoli.application.follow;

import java.time.LocalDateTime;
import java.util.Objects;

public class FollowInfo {
    private final Long id;

    private final Long userId;
    private final String nickName;
    private final LocalDateTime createdAt;

    private boolean subscribed = true;


    public FollowInfo(Long id, Long userId, String nickName, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.nickName = nickName;
        this.createdAt = createdAt;
    }

    public boolean notSubscribed() {
        return this.subscribed = false;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public String getNickName() {
        return nickName;
    }

    public String getCreatedAt() {
        return createdAt.toString();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowInfo)) return false;
        FollowInfo that = (FollowInfo) o;
        return subscribed == that.subscribed && Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(nickName, that.nickName) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, nickName, createdAt, subscribed);
    }

    @Override
    public String toString() {
        return "FollowInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", createdAt=" + createdAt +
                ", subscribed=" + subscribed +
                '}';
    }
}
