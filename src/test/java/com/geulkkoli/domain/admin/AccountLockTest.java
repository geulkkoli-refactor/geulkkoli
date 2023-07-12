package com.geulkkoli.domain.admin;

import com.geulkkoli.domain.user.User;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AccountLockTest {
    /**
     * accountLockTest
     * 1. 정지사유
     * 2. 정지기간
     * 3. 정지된 유저
     */

    @Test
    void lockTest() {
        User user = User.builder().userName("김").nickName("test").gender("male")
                .phoneNo("01012345678").email("tak1@gmail.com")
                .phoneNo("01012345678").build();

        AccountLock accountLock = AccountLock.of(user, "정지사유", LocalDateTime.now().plusDays(7));

        assertAll(() -> assertThat(accountLock).has(new Condition<>(a -> a.getReason().equals("정지사유"), "정지사유")),
                () -> assertThat(accountLock).has(new Condition<>(a -> a.getLockExpirationDate().isAfter(LocalDateTime.now()), "lockExpirationDate")),
                () -> assertThat(accountLock).has(new Condition<>(a -> a.getLockedUser().equals(user), "lockedUser")));

    }
}
