package com.geulkkoli.application.social.service;

import com.geulkkoli.domain.social.service.SocialInfo;
import com.geulkkoli.domain.social.service.SocialInfoFindService;
import com.geulkkoli.domain.social.service.SocialInfoService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.web.social.SocialInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j

@Service
@RequiredArgsConstructor
public abstract class AbstractOauth2UserService {
    private final UserFindService userFindService;

    private final SocialInfoFindService socialInfoFindService;
    private final SocialInfoService socialInfoService;

    public Boolean isSignUp(String email) {
        Optional<User> signUpUser =userFindService.findByEmail(email);
        return signUpUser.isPresent();
    }

    public User userInfo(String email) {
        return userFindService.findByEmail(email).orElseThrow(() -> new NoSuchElementException("No user found email matches:" + email));
    }

    public void connect(String socialId, String socialType, User user) {
        SocialInfoDto socialInfoDto = SocialInfoDto.of(socialId, socialType, user);
        socialInfoService.connect(socialInfoDto);
    }

    public Boolean haveAssociatedRecord(String socialId, String socialType) {
        return socialInfoFindService.isAssociatedRecord(socialType, socialId);
    }

    public Boolean isConnected(String socialId) {
        return socialInfoFindService.isConnected(socialId);
    }

    @Transactional
    public Boolean reConnected(String socialId, String socialType) {
        SocialInfo socialInfo = socialInfoFindService.findBySocialTypeAndSocialId(socialType, socialId);
        socialInfoService.reconnect(socialInfo);
        return true;
    }
    public User findUserBySocialId(String socialId, String socialType) {
        return socialInfoFindService.findBySocialTypeAndSocialId(socialType, socialId).getUser();
    }
}
