package com.geulkkoli.application.user;

import com.geulkkoli.application.security.AccountStatus;
import com.geulkkoli.application.social.util.SocialType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
@Getter
public class CustomAuthenticationPrinciple implements UserDetails, OAuth2User {
    private final String userId; // 권한을 부여한 서버의 아이디
    private final String userName; //로그인 시 아이디에 해당한다. 우리 서비스의 경우 email
    private final String password;
    private final boolean isEnabled;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final Collection<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private String userRealName;
    private String nickName;
    private String phoneNo;
    private String gender;
    private SocialType socialType;

    private CustomAuthenticationPrinciple(UserModelDto userModel, Collection<GrantedAuthority> authorities, AccountStatus accountStatus, Map<String, Object> attributes) {
        this.userName = userModel.getEmail();
        this.password = userModel.getPassword();
        this.authorities = authorities;
        this.nickName = userModel.getNickName();
        this.userId = userModel.getAuthorizaionServerId();
        this.gender = userModel.getGender();
        this.phoneNo = userModel.getPhoneNo();
        this.userRealName = userModel.getUserName();
        this.isAccountNonExpired = accountStatus.isAccountNonExpired();
        this.isAccountNonLocked = accountStatus.isAccountNonLocked();
        this.isCredentialsNonExpired = accountStatus.isCredentialsNonExpired();
        this.isEnabled = accountStatus.isEnabled();
        this.attributes = attributes;
    }

    private CustomAuthenticationPrinciple(UserModelDto userModel, Collection<GrantedAuthority> authorities, AccountStatus accountStatus, Map<String, Object> attributes, SocialType socialType) {
        this.userName = userModel.getEmail();
        this.password = userModel.getPassword();
        this.authorities = authorities;
        this.nickName = userModel.getNickName();
        this.userId = userModel.getAuthorizaionServerId();
        this.gender = userModel.getGender();
        this.phoneNo = userModel.getPhoneNo();
        this.userRealName = userModel.getUserName();
        this.isAccountNonExpired = accountStatus.isAccountNonExpired();
        this.isAccountNonLocked = accountStatus.isAccountNonLocked();
        this.isCredentialsNonExpired = accountStatus.isCredentialsNonExpired();
        this.isEnabled = accountStatus.isEnabled();
        this.attributes = attributes;
        this.socialType = socialType;
    }


    public static CustomAuthenticationPrinciple from(UserModelDto userModel, Collection<GrantedAuthority> authorities, AccountStatus accountStatus) {
        return new CustomAuthenticationPrinciple(userModel, authorities, accountStatus, Map.of());
    }

    public static CustomAuthenticationPrinciple from(UserModelDto userModel, Collection<GrantedAuthority> authorities, AccountStatus accountStatus, Map<String, Object> attributes) {
        return new CustomAuthenticationPrinciple(userModel, authorities, accountStatus, attributes);
    }

    public static CustomAuthenticationPrinciple from(UserModelDto userModel, Collection<GrantedAuthority> authorities, AccountStatus accountStatus, Map<String, Object> attributes, SocialType socialType) {
        return new CustomAuthenticationPrinciple(userModel, authorities, accountStatus, attributes, socialType);
    }

    public void modifyNickName(String nickName) {
        this.nickName = nickName;
    }

    public void modifyPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void modifyGender(String gender) {
        this.gender = gender;
    }

    public void modifyUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    //사용자 계정이 만료되었는지 여부를 나타냅니다. 만료된 계정은 인증할 수 없습니다.
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    //사용자가 잠겨 있는지 또는 잠금 해제되었는지 여부를 나타냅니다. 잠긴 사용자는 인증할 수 없습니다.
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    //사용자의 자격증명(비밀번호)이 만료되었는지 여부를 나타냅니다. 자격 증명이 만료되면 인증이 불가능합니다.
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    //사용자가 활성화되었는지 또는 비활성화되었는지를 나타냅니다. 비활성화된 사용자는 인증할 수 없습니다.
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public String getUserName() {
        return userName;
    }



    public String getNickName() {
        return nickName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomAuthenticationPrinciple)) return false;
        CustomAuthenticationPrinciple authUser = (CustomAuthenticationPrinciple) o;
        return Objects.equals(userName, authUser.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return "CustomAuthenticationPrinciple{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", authorities=" + authorities +
                ", attributes=" + attributes +
                ", userRealName='" + userRealName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender='" + gender + '\'' +
                ", socialType=" + socialType +
                '}';
    }
}
