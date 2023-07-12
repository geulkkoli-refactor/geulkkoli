package com.geulkkoli.application.follow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FollowInfoTest {

    @Test
    void checkSubscribe() {
        FollowInfo followInfo = new FollowInfo(1L, 1L, "geulkkoli", null);
        followInfo.notSubscribed();

        assertFalse(followInfo.isSubscribed());
    }
}