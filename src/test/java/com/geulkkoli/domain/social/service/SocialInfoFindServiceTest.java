package com.geulkkoli.domain.social.service;

import com.geulkkoli.application.social.util.SocialType;
import com.geulkkoli.domain.social.NoSuchSocialInfoException;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.social.SocialInfoDto;
import com.geulkkoli.web.user.dto.mypage.ConnectedSocialInfos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class SocialInfoFindServiceTest {
    @Autowired
    SocialInfoFindService socialInfoFindService;
    @Autowired
    SocialInfoService socialInfoService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("연동된 소셜 정보를 소셜 타입과 소셜 아이디로 찾는다.")
    @Test
    void findBySocialTypeAndSocialId() {
        User user = User.builder()
                .email("test12@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testType", user);
        socialInfoService.connect(socialInfoDto);

        SocialInfo socialInfo = socialInfoFindService.findBySocialTypeAndSocialId("testType", "testId");

        assertAll(
                () -> assertEquals(socialInfo.getSocialId(), socialInfoDto.getAuthorizationServerId()),
                () -> assertEquals(socialInfo.getSocialType(), socialInfoDto.getClientregistrationName()),
                () -> assertEquals(socialInfo.getUser(), socialInfoDto.getUser())
        );
    }

    @DisplayName("연동된 소셜 정보를 소셜 타입과 소셜 아이디로 찾는다. 연동된 정보가 없으면 예외를 던진다.")
    @Test
    void findBySocialTypeAndSocialId_연동된_정보가_없으면_예외를_던진다() {
        assertThrows(NoSuchSocialInfoException.class, () -> socialInfoFindService.findBySocialTypeAndSocialId("testType", "testId"));
    }

    @DisplayName("연동을 유지한 상태에서 연동한 기록이 있는지 확인한다.")
    @Test
    void isAssociatedRecord_when_connected() {
        {
            User user = User.builder()
                    .email("test32@gmail.com")
                    .userName("test")
                    .password("123")
                    .phoneNo("01133345555")
                    .gender("Male")
                    .nickName("test")
                    .build();
            userRepository.save(user);
            SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testType", user);
            socialInfoService.connect(socialInfoDto);

            assertTrue(socialInfoFindService.isAssociatedRecord("testType", "testId"));
        }
    }

    @DisplayName("연동을 유지하지 않았고 연동한 기록이 있는지 확인한다.")
    @Test
    void isAssociatedRecord_when_disconnected() {
        User user = User.builder()
                .email("test32@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testType", user);
        socialInfoService.connect(socialInfoDto);
        socialInfoService.disconnect("testName", "testType");

        assertTrue(socialInfoFindService.isAssociatedRecord("testType", "testId"));
    }

    @DisplayName("소셜 아이디로 현재 연동되어 있는지 확인한다.")
    @Test
    void isConnected() {
        User user = User.builder()
                .email("test33@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testType", user);
        socialInfoService.connect(socialInfoDto);

        assertTrue(socialInfoFindService.isConnected("testId"));
    }

    @DisplayName("이메일로 모든 소셜 연동 정보를 찾는다.")
    @Test
    void findConnectedInfosByEmail() {
        User user = User.builder()
                .email("test34@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", SocialType.KAKAO.getValue(), user);
        SocialInfoDto socialInfoDto2 = SocialInfoDto.of("testId2", SocialType.GOOGLE.getValue(), user);
        SocialInfoDto socialInfoDto3 = SocialInfoDto.of("testId3", SocialType.NAVER.getValue(), user);
        socialInfoService.connect(socialInfoDto);
        socialInfoService.connect(socialInfoDto2);
        socialInfoService.connect(socialInfoDto3);

        ConnectedSocialInfos connectedInfos = socialInfoFindService.findConnectedInfosByEmail(user.getEmail());

        assertAll(
                () -> assertEquals(3, connectedInfos.getConnectedSocialInfosSize()),
                () -> assertTrue(connectedInfos.isGoogleConnected()),
                () -> assertTrue(connectedInfos.isNaverConnected()),
                () -> assertTrue(connectedInfos.isKakaoConnected())
        );

    }

    @DisplayName("닉네임으로 모든 소셜 연동 정보를 찾는다.")
    @Test
    void findConnectedInfosByNickName() {
        User user = User.builder()
                .email("test54@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", SocialType.KAKAO.getValue(), user);
        SocialInfoDto socialInfoDto2 = SocialInfoDto.of("testId2", SocialType.GOOGLE.getValue(), user);
        SocialInfoDto socialInfoDto3 = SocialInfoDto.of("testId3", SocialType.NAVER.getValue(), user);
        socialInfoService.connect(socialInfoDto);
        socialInfoService.connect(socialInfoDto2);
        socialInfoService.connect(socialInfoDto3);

        ConnectedSocialInfos connectedInfos = socialInfoFindService.findConnectedInfosByNickName(user.getNickName());

        assertAll(
                () -> assertEquals(3, connectedInfos.getConnectedSocialInfosSize()),
                () -> assertTrue(connectedInfos.isGoogleConnected()),
                () -> assertTrue(connectedInfos.isNaverConnected()),
                () -> assertTrue(connectedInfos.isKakaoConnected())
        );
    }

}