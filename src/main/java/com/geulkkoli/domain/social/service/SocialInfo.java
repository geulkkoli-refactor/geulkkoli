package com.geulkkoli.domain.social.service;

import com.geulkkoli.domain.user.User;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;


@Entity
@Table(name = "social_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"social_id", "social_type"})})
public class SocialInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_info_id")
    private Long socialInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "social_id")
    private String socialId;

    @Column(nullable = false, name = "social_type")
    private String socialType;

    @Column
    @CreatedDate
    private String socialConnectDate;

    @Column(columnDefinition = "boolean default true")
    private Boolean isConnected = true;

    public SocialInfo() {
    }

    @Builder
    public SocialInfo(User user, String socialId, String socialType, String socialConnectDate) {
        this.user = user;
        this.socialId = socialId;
        this.socialType = socialType;
        this.socialConnectDate = socialConnectDate;
    }

    protected void disconnect() {
        this.isConnected = false;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public User getUser() {
        return user;
    }

    public Long getSocialInfoId() {
        return socialInfoId;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getSocialType() {
        return socialType;
    }

    public String getSocialConnectDate() {
        return socialConnectDate;
    }


    public Boolean reConnected(boolean b) {
        return this.isConnected = b;
    }
}
