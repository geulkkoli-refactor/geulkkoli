package com.geulkkoli.domain.admin;

import com.geulkkoli.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 계정 잠금 이력을 담당하는 엔티티 입니다.
 * 계정 잠금 이력은 관리자가 계정을 잠금시킬 때 생성됩니다.
 * lockExpirationDate 필드는 계정 잠금이 해제되는 시간을 나타냅니다.
 * 한 계정이 여러번 잠길 수 있으므로 일대다 연관관계로 설정했습니다.
 * reason 필드는 계정 잠금 이력을 생성한 관리자가 잠금 이유를 적을 수 있습니다.
 * 계정이 잠금 이력 일정 횟수 이상 도달하면 계정 자체를 비활성화 시킬 생각입니다.
 *
 */
@Entity
public class AccountLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User lockedUser;
    @Column(nullable = false, length = 200)
    private String reason;

    @Column(nullable = false, name = "lock_until")
    private LocalDateTime lockExpirationDate;

    public AccountLock() {

    }

    private AccountLock(User user, String reason, LocalDateTime localDateTime) {
        this.lockedUser = user;
        this.reason = reason;
        this.lockExpirationDate = localDateTime;
    }

    public static AccountLock of(User user, String reason, LocalDateTime localDateTime) {
        return new AccountLock(user, reason, localDateTime);
    }

    public User getLockedUser() {
        return lockedUser;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getLockExpirationDate() {
        return lockExpirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountLock)) return false;
        AccountLock that = (AccountLock) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
