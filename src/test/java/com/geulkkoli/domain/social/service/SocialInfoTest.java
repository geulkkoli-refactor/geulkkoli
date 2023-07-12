package com.geulkkoli.domain.social.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SocialInfoTest {

    @DisplayName("소셜 정보 공급자를 얻는다.")
    @Test
    void getSocialType() {
        SocialInfo socialInfo = SocialInfo.builder()
                .socialId("kakao")
                .socialType("kakao")
                .socialConnectDate(LocalDateTime.of(2021, 5, 23, 0, 0).toString())
                .build();

        assertEquals("kakao", socialInfo.getSocialType());
    }

    @DisplayName("연동의 기본값은 true이다.")
    @Test
    void getConnected() {
        SocialInfo socialInfo = SocialInfo.builder()
                .socialId("kakao")
                .socialType("kakao")
                .socialConnectDate(LocalDateTime.of(2021, 5, 23, 0, 0).toString())
                .build();

        assertTrue(socialInfo.getConnected());
    }

    @DisplayName("소셜 연동 정보를 끊는다.")
    @Test
    void disconnect() {
        SocialInfo socialInfo = SocialInfo.builder()
                .socialId("kakao")
                .socialType("kakao")
                .socialConnectDate(LocalDateTime.of(2021, 5, 23, 0, 0).toString())
                .build();
        socialInfo.disconnect();

        assertFalse(socialInfo.getConnected());
    }

    @DisplayName("소셜 연동 정보를 다시 연결한다.")
    @Test
    void reConnected() {
        SocialInfo socialInfo = SocialInfo.builder()
                .socialId("kakao")
                .socialType("kakao")
                .socialConnectDate(LocalDateTime.of(2021, 5, 23, 0, 0).toString())
                .build();
        socialInfo.disconnect();
        socialInfo.reConnected(true);

        assertTrue(socialInfo.getConnected());
    }

}