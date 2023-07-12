package com.geulkkoli.web.user.dto.mypage;

public class GoogleConnectedSocialInfo implements ConnectSocialInfo {
    private String socialType;
    private Boolean connected;

    public GoogleConnectedSocialInfo(String socialType, Boolean connected) {
        this.socialType = socialType;
        this.connected = connected;
    }

    @Override
    public String getSocialType() {
        return this.socialType;
    }

    @Override
    public Boolean isConnect() {
        return connected;
    }
}
