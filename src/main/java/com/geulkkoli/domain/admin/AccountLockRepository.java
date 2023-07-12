package com.geulkkoli.domain.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountLockRepository extends JpaRepository<AccountLock, Long> {

    @Query("select a from AccountLock a where a.lockedUser.userId = :userId")
    Optional<AccountLock> findByUserId(@Param("userId") Long userId);

    @Query("select count(a.lockedUser) > 0 from AccountLock a where a.lockedUser.userId = :userId")
    Boolean existsByLockedUser_UserId(@Param("userId") Long userId);


    @Override
    void delete(AccountLock entity);

}
