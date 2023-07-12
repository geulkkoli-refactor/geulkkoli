package com.geulkkoli.application.security;

/**
 * 계정 상태를 나타내는 Enum
 * 각각 계정이 활성화 되어있는지, 비밀번호가 만료되었는지, 계정이 만료되었는지, 계정이 잠겨있는지를 나타낸다.
 */
public enum AccountStatus {
    PASSWORD_EXPIRED(true,true,true,false),
    LOCKED(true,true,false,true),
    ACCOUNT_EXPIRED(true,false,true,true),
    DISABLED(false,true,true,true),
    ACTIVE(true,true,true,true);

    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    AccountStatus(boolean isEnabled, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired) {
        this.isEnabled = isEnabled;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }
}
