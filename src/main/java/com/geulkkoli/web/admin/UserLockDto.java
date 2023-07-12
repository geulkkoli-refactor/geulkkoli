package com.geulkkoli.web.admin;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.PositiveOrZero;

@Getter
public class UserLockDto {

    private final Long postId;

    @Length(max = 200)
    private final String lockReason;

    @PositiveOrZero
    private final Long lockDate;

    public UserLockDto(Long postId, String lockReason, Long lockDate) {
        this.postId = postId;
        this.lockReason = lockReason;
        this.lockDate = lockDate;
    }
}
