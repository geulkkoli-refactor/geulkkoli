package com.geulkkoli.application.follow;

import java.util.List;
import java.util.stream.Collectors;

public class FollowInfos {
    private final List<FollowInfo> followInfos;

    private FollowInfos(List<FollowInfo> followInfos) {
        this.followInfos = followInfos;
    }

    public static FollowInfos of(List<FollowInfo> followInfos) {
        return new FollowInfos(followInfos);
    }

    public void checkSubscribe(List<Long> userIds) {
        followInfos.forEach(followInfo -> {
            if (!userIds.contains(followInfo.getUserId())) {
                followInfo.notSubscribed();
            }
        });
    }

    public List<FollowInfo> getFollowInfos() {
        return followInfos;
    }

    public List<FollowInfo> followInfos(){
        return followInfos;
    }

    public List<Long> userIds(){
        return followInfos.stream().map(FollowInfo::getUserId)
                .collect(Collectors.toUnmodifiableList());
    }
}
