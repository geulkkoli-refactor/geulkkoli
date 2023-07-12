package com.geulkkoli.domain.social.service;

import com.geulkkoli.application.social.util.SocialType;
import com.geulkkoli.domain.social.NoSuchSocialInfoException;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.social.SocialInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class SocialInfoServiceTest {

    @Autowired
    SocialInfoService socialInfoService;
    @Autowired
    SocialInfoFindService socialInfoFindService;
    @Autowired
    UserRepository userRepository;

    @DisplayName("소셜 정보를 연동한다.")
    @Test
    void connect() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testName", user);
        SocialInfo connected = socialInfoService.connect(socialInfoDto);

        assertAll(
                () -> assertEquals(connected.getSocialId(), socialInfoDto.getAuthorizationServerId()),
                () -> assertEquals(connected.getSocialType(), socialInfoDto.getClientregistrationName()),
                () -> assertEquals(connected.getUser(), socialInfoDto.getUser())
        );
    }

    @DisplayName("소셜 정보를 연동을 해제한다. 이때 연동 여부만 false로 바꾼다. 나머지 정보는 유지된다.")
    @Test
    void disconnect() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testName", user);
        socialInfoService.connect(socialInfoDto);
        socialInfoService.disconnect("test@gmail.com", "testName");
        SocialInfo findSocialInfo = socialInfoFindService.findBySocialTypeAndSocialId("testName", "testId");

        assertAll(
                () -> assertFalse(findSocialInfo.getConnected()),
                () -> assertThat(findSocialInfo.getSocialId()).isEqualTo("testId"),
                () -> assertThat(findSocialInfo.getSocialType()).isEqualTo("testName"),
                () -> assertThat(findSocialInfo.getUser()).isEqualTo(user));
    }


    @DisplayName("소셜 정보를 재연동한다.")
    @Test
    void reconnect() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testName", user);
        socialInfoService.connect(socialInfoDto);
        socialInfoService.disconnect("test@gmail.com", "testName");
        SocialInfo socialInfo = socialInfoFindService.findBySocialTypeAndSocialId("testName", "testId");
        SocialInfo reconnect = socialInfoService.reconnect(socialInfo);

        assertAll(
                () -> assertTrue(reconnect.getConnected()),
                () -> assertThat(reconnect.getSocialId()).isEqualTo("testId"),
                () -> assertThat(reconnect.getSocialType()).isEqualTo("testName"),
                () -> assertThat(reconnect.getUser()).isEqualTo(user));
    }

    @DisplayName("소셜 정보를 완전히 삭제한다.")
    @Test
    void delete() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .password("123")
                .phoneNo("01133345555")
                .gender("Male")
                .nickName("test")
                .build();
        userRepository.save(user);
        SocialInfoDto socialInfoDto = SocialInfoDto.of("testId", "testName", user);
        SocialInfo connected = socialInfoService.connect(socialInfoDto);
        socialInfoService.delete(connected);

        assertThatThrownBy(() -> socialInfoFindService.findBySocialTypeAndSocialId("testName", "testId"))
                .isInstanceOf(NoSuchSocialInfoException.class)
                .hasMessageContaining("해당 소셜 정보가 존재하지 않습니다.");
    }

    @DisplayName("모든 소셜 정보를 삭제한다.")
    @Test
    void deleteAll() {
        User user = User.builder()
                .email("test@gmail.com")
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

        socialInfoService.deleteAll(user.getEmail());

        assertAll(
                () -> assertThatThrownBy(() -> socialInfoFindService.findBySocialTypeAndSocialId(SocialType.KAKAO.getValue(), "testId"))
                        .isInstanceOf(NoSuchSocialInfoException.class)
                        .hasMessageContaining("해당 소셜 정보가 존재하지 않습니다."),
                () -> assertThatThrownBy(() -> socialInfoFindService.findBySocialTypeAndSocialId(SocialType.GOOGLE.getValue(), "testId2"))
                        .isInstanceOf(NoSuchSocialInfoException.class)
                        .hasMessageContaining("해당 소셜 정보가 존재하지 않습니다."),
                () -> assertThatThrownBy(() -> socialInfoFindService.findBySocialTypeAndSocialId(SocialType.NAVER.getValue(), "testId3"))
                        .isInstanceOf(NoSuchSocialInfoException.class)
                        .hasMessageContaining("해당 소셜 정보가 존재하지 않습니다."));
    }
}
