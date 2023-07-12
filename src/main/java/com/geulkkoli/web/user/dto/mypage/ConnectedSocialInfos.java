package com.geulkkoli.web.user.dto.mypage;

import com.geulkkoli.application.social.util.SocialType;
import com.geulkkoli.application.social.util.SocialTypeException;
import com.geulkkoli.domain.social.service.SocialInfo;

import java.util.List;
import java.util.stream.Collectors;

public class ConnectedSocialInfos {

    private List<ConnectSocialInfo> connectedSocialInfos;


    public ConnectedSocialInfos(List<SocialInfo> socialInfos) {
        this.connectedSocialInfos = socialInfos.stream()
                .map(i -> convert(i.getSocialType(), i.getConnected()))
                .collect(Collectors.toList());
    }

    private ConnectSocialInfo convert(String socialType, Boolean connected) {
        if (SocialType.NAVER.getValue().equals(socialType)) {
            return new NaverConnectedSocialInfo(socialType, connected);
        }
        if (SocialType.KAKAO.getValue().equals(socialType)) {
            return new KaKaoConnectedSocialInfo(socialType, connected);
        }
        if (SocialType.GOOGLE.getValue().equals(socialType)) {
            return new GoogleConnectedSocialInfo(socialType, connected);
        }
        throw new SocialTypeException("해당하는 소셜 타입이 없습니다.");
    }

    public Boolean isKakaoConnected() {
        return connectedSocialInfos.stream().filter(con -> con.getSocialType().equals(SocialType.KAKAO.getValue())).map(ConnectSocialInfo::isConnect).findFirst().orElse(false);
    }

    public Boolean isNaverConnected() {
        return connectedSocialInfos.stream().filter(con -> con.getSocialType().equals(SocialType.NAVER.getValue())).map(ConnectSocialInfo::isConnect).findFirst().orElse(false);

    }

    public Boolean isGoogleConnected() {
        return connectedSocialInfos.stream().filter(con -> con.getSocialType().equals(SocialType.GOOGLE.getValue())).map(ConnectSocialInfo::isConnect).findFirst().orElse(false);

    }

    public int getConnectedSocialInfosSize() {
        return connectedSocialInfos.size();

    }
}
