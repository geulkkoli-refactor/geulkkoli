package com.geulkkoli.application.follow;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class FollowInfosTest {

    @Test
    void checkSubscribe() {
        FollowInfos followInfos = FollowInfos.of(
                List.of(
                        new FollowInfo(1L, 1L, "geulkkoli", LocalDateTime.now()),
                        new FollowInfo(2L, 2L, "geulkkoli2", LocalDateTime.now()),
                        new FollowInfo(3L, 3L, "geulkkoli3", LocalDateTime.now())
                )
        );

        followInfos.checkSubscribe(List.of(1L, 2L));


        assertAll(() -> {
            assertThat(followInfos.followInfos().get(0).isSubscribed()).isTrue();
            assertThat(followInfos.followInfos().get(1).isSubscribed()).isTrue();
            assertThat(followInfos.followInfos().get(2).isSubscribed()).isFalse();
        });

    }
}