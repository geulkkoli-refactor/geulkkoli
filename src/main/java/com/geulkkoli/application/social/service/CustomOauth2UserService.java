package com.geulkkoli.application.social.service;

import com.geulkkoli.application.security.AccountStatus;
import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.social.util.SocialType;
import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.application.user.ProviderUser;
import com.geulkkoli.application.user.UserModelDto;
import com.geulkkoli.application.social.util.DelegateOAuth2RequestConverter;
import com.geulkkoli.application.social.util.ProviderUserRequest;
import com.geulkkoli.domain.social.service.SocialInfoFindService;
import com.geulkkoli.domain.social.service.SocialInfoService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Boolean.*;

@Slf4j
@Service
@Transactional
public class CustomOauth2UserService extends AbstractOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    public CustomOauth2UserService(UserFindService userFindService, SocialInfoFindService socialInfoFindService, SocialInfoService socialInfoService) {
        super(userFindService, socialInfoFindService, socialInfoService);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(userRequest.getClientRegistration(), oAuth2User);
        DelegateOAuth2RequestConverter delegateOAuth2RequestConverter = new DelegateOAuth2RequestConverter();
        ProviderUser providerUser = delegateOAuth2RequestConverter.convert(providerUserRequest);
        List<GrantedAuthority> authorities = new ArrayList<>();

        boolean isSignUp = TRUE.equals(isSignUp(providerUser.getEmail()));
        boolean isAssociated = TRUE.equals(haveAssociatedRecord(providerUser.getId(), providerUser.getProvider()));
        boolean isConnected = TRUE.equals(isConnected(providerUser.getId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 로그인 상태에서 깉은 이메일을 가진 소셜 계정 연동한 기록이 아예 없는 경우
        if (Objects.nonNull(authentication) && isSignUp && !isAssociated) {
            User user = userInfo(providerUser.getEmail());
            UserModelDto model = singedUpUserToModel(user);

            connect(providerUser.getId(), providerUser.getProvider(), user);
            authorities.add(new SimpleGrantedAuthority(user.roleName()));
            return CustomAuthenticationPrinciple.from(model, authorities, AccountStatus.ACTIVE, providerUser.getAttributes(), SocialType.findByProviderName(providerUser.getProvider()));
        }

        // 로그인 상태에서 이메일이 다른 소셜 계정이 연동되어 있지 않은 경우
        if (Objects.nonNull(authentication) && !isSignUp && !isAssociated) {
            return differentEmailConnectAndLogin(providerUser, authorities, authentication);
        }

        // 로그인한 상태에서 같은 이메일로 회원가입이 되어 있고 소셜 연동을 한 기록이 있고 소셜 연동을 유지하지 않은 경우
        if (Objects.nonNull(authentication) && isSignUp && !isConnected) {
            return reConnectAndLogin(providerUser, authorities, authentication);
        }

        // 로그인 상태에서 같은 이메일로 회원가입이 되어 있지 않고 소셜 연동을 한 기록이 있고 소셜 연동을 유지하지 않은 경우
        if (Objects.nonNull(authentication) && !isSignUp && !isConnected) {
            return reConnectAndLogin(providerUser, authorities, authentication);
        }

        // 같은 이메일로 회원가입이 되어 있고 소셜 연동을 한 기록이 있고 소셜 연동을 유지한 경우
        if (isSignUp && isAssociated && isConnected) {
            User user = userInfo(providerUser.getEmail());
            return checkLockAndLogin(providerUser, authorities, user);
        }


        // 이메일이 다른 소셜 계정으로 로그인을 시도했고 연동한 기록이 있고 소셜 연동을 유지한 경우
        if (!isSignUp && isAssociated && isConnected) {
            User user = findUserBySocialId(providerUser.getId(), providerUser.getProvider());
            return checkLockAndLogin(providerUser, authorities, user);
        }


        // 회원가입을 아예 하지 않은 경우
        if (!isSignUp && !isAssociated) {
            UserModelDto model = provideUserToModel(providerUser);
            authorities.add(new SimpleGrantedAuthority(Role.GUEST.getRoleName()));
            return CustomAuthenticationPrinciple.from(model, authorities, AccountStatus.ACTIVE, providerUser.getAttributes(), SocialType.findByProviderName(providerUser.getProvider()));
        }

        // 이메일이 다른 소셜 계정으로 로그인을 시도했고 연동한 기록이 있고 소셜 연동을 유지하지 않은 경우
        throw new OAuth2AuthenticationException("소셜 계정 연동이 되어 있지 않습니다.");
    }

    private OAuth2User reConnectAndLogin(ProviderUser providerUser, List<GrantedAuthority> authorities, Authentication authentication) {
        CustomAuthenticationPrinciple principle = (CustomAuthenticationPrinciple) authentication.getPrincipal();
        reConnected(providerUser.getId(), providerUser.getProvider());
        User user = userInfo(principle.getUsername());
        UserModelDto model = singedUpUserToModel(user);
        authorities.add(new SimpleGrantedAuthority(user.roleName()));
        return CustomAuthenticationPrinciple.from(model, authorities, AccountStatus.ACTIVE, providerUser.getAttributes(), SocialType.findByProviderName(providerUser.getProvider()));
    }

    private OAuth2User checkLockAndLogin(ProviderUser providerUser, List<GrantedAuthority> authorities, User user) {
        UserModelDto model = singedUpUserToModel(user);
        if (TRUE.equals(user.isLock())) {
            return CustomAuthenticationPrinciple.from(model, authorities, AccountStatus.LOCKED);
        }
        authorities.add(new SimpleGrantedAuthority(user.roleName()));
        return CustomAuthenticationPrinciple.from(model, authorities, AccountStatus.ACTIVE, providerUser.getAttributes(), SocialType.findByProviderName(providerUser.getProvider()));
    }

    private OAuth2User differentEmailConnectAndLogin(ProviderUser providerUser, List<GrantedAuthority> authorities, Authentication authentication) {
        CustomAuthenticationPrinciple principle = (CustomAuthenticationPrinciple) authentication.getPrincipal();
        User user = userInfo(principle.getUsername());
        UserModelDto model = singedUpUserToModel(user);
        connect(providerUser.getId(), providerUser.getProvider(), user);
        authorities.add(new SimpleGrantedAuthority(user.roleName()));
        return CustomAuthenticationPrinciple.from(model, authorities, AccountStatus.ACTIVE, providerUser.getAttributes(), SocialType.findByProviderName(providerUser.getProvider()));
    }

    private UserModelDto provideUserToModel(ProviderUser providerUser) {

        return UserModelDto.builder()
                .authorizaionServerId(providerUser.getId())
                .gender(providerUser.getGender())
                .userName(providerUser.getUsername())
                .email(providerUser.getEmail())
                .nickName(providerUser.getNickName())
                .password(providerUser.getPassword())
                .phoneNo("0100000000").build();

    }

    private UserModelDto singedUpUserToModel(User user) {
        return UserModelDto.toDto(user);
    }

}
