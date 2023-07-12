package com.geulkkoli.application.security;

import lombok.Getter;

import java.util.Arrays;

/**
 * 역할을 나타내는 enum 입니다.
 * role code는 실제 db에 저장되는 값입니다.
 * role name은 역할을 나타내는 값으로 security에서 이 역할을 읽고 각 클래스 또는 메서드 단위로 인증을 확인할 수 있습니다.
 */
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", 1),
    USER("ROLE_USER", 2),
    GUEST("ROLE_GUEST", 3);

    private final String roleName;
    private final int roleCode;

    Role(String roleName, int roleCode) {
        this.roleName = roleName;
        this.roleCode = roleCode;
    }

    public static Role findByRoleName(String role) {
        return Arrays.stream(values()).filter(r -> r.getRoleName().equals(role)).findAny().orElse(GUEST);
    }

    public static boolean isUser(String role) {
        return USER.getRoleName().equals(role);
    }

    public static boolean isAdmin(String role) {
        return ADMIN.getRoleName().equals(role);
    }

    public static boolean isGuest(String role) {
        return GUEST.getRoleName().equals(role);
    }
}
