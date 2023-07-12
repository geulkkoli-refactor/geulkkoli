package com.geulkkoli.domain.follow;

import com.geulkkoli.domain.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_followings", uniqueConstraints = @UniqueConstraint(columnNames = {"followee_id", "follower_id"}))
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followings_id")
    private Long id;
    // follower가 followee를 팔로우한다.
    @ManyToOne
    @JoinColumn(name = "followee_id")
    private User followee;
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public Follow() {
    }


    private Follow(User followee, User follower) {
        this.followee = followee;
        this.follower = follower;
    }

    public static Follow of(User followee, User follower) {
        return new Follow(followee, follower);
    }


    public Long getId() {
        return id;
    }

    public User getFollowee() {
        return followee;
    }

    public User getFollower() {
        return follower;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Follow)) return false;
        Follow that = (Follow) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", followee=" + followee.getNickName() +
                ", follower=" + follower.getNickName() +
                ", createdAt=" + createdAt +
                '}';
    }
}
