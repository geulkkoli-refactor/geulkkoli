package com.geulkkoli.domain.social.service;

import com.geulkkoli.application.social.util.SocialType;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.social.SocialInfoDto;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
class SocialInfoRepositoryTest {
    @Autowired
    private SocialInfoRepository socialInfoRepository;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("소셜 타입과 소셜 아이디로 소셜 정보를 찾는다.")
    @Test
    void findSocialInfoBySocialTypeAndSocialId() {
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

        SocialInfo save = socialInfoRepository.save(socialInfoDto.toEntity());

        Optional<SocialInfo> testId = socialInfoRepository.findSocialInfoBySocialTypeAndSocialId(SocialType.KAKAO.getValue(), "testId");

        assertAll(
                () -> assertThat(testId).hasValue(save).get().has(new Condition<>(s -> s.getSocialId().equals("testId"), "testId")),
                () -> assertThat(testId).hasValue(save).get().has(new Condition<>(s -> s.getSocialType().equals(SocialType.KAKAO.getValue()), SocialType.KAKAO.getValue())),
                () -> assertThat(testId).hasValue(save).get().is(new Condition<>(s -> s.getUser().equals(user), "test"))
                );
    }

    @DisplayName("소셜 타입과 유저 이메일로 소셜 정보를 찾는다.")
    @Test
    void findSocialInfoBySocialTypeAndAndUser_Email() {
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
        SocialInfo save = socialInfoRepository.save(socialInfoDto.toEntity());
        SocialInfo save1 = socialInfoRepository.save(socialInfoDto2.toEntity());
        SocialInfo save2 = socialInfoRepository.save(socialInfoDto3.toEntity());

        Optional<SocialInfo> test = socialInfoRepository.findSocialInfoBySocialTypeAndAndUser_Email(SocialType.KAKAO.getValue(), user.getEmail());

        assertAll(
                () -> assertThat(test).hasValue(save).get().has(new Condition<>(s -> s.getSocialId().equals("testId"), "testId")),
                () -> assertThat(test).hasValue(save).get().has(new Condition<>(s -> s.getSocialType().equals(SocialType.KAKAO.getValue()), SocialType.KAKAO.getValue())),
                () -> assertThat(test).hasValue(save).get().is(new Condition<>(s -> s.getUser().equals(user), "test"))
        );

    }

    @DisplayName("소셜 타입과 소셜 아이디로 소셜 정보가 존재하는지 확인")
    @Test
    void existsBySocialTypeAndSocialId() {
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
        SocialInfo save = socialInfoRepository.save(socialInfoDto.toEntity());

        assertAll(
                () -> assertThat(socialInfoRepository.existsBySocialTypeAndSocialId(SocialType.KAKAO.getValue(), "testId")).isTrue(),
                () -> assertThat(socialInfoRepository.existsBySocialTypeAndSocialId(SocialType.GOOGLE.getValue(), "testId2")).isFalse(),
                () -> assertThat(socialInfoRepository.existsBySocialTypeAndSocialId(SocialType.NAVER.getValue(), "testId3")).isFalse()
        );
    }

    @DisplayName("회원 이메일로 모든 회원의 소셜정보를 가져온다.")
    @Test
    void findAllByUserEmail() {
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
        SocialInfo save = socialInfoRepository.save(socialInfoDto.toEntity());
        SocialInfo save1 = socialInfoRepository.save(socialInfoDto2.toEntity());
        SocialInfo save2 = socialInfoRepository.save(socialInfoDto3.toEntity());

        List<SocialInfo> allByUserEmail = socialInfoRepository.findAllByUserEmail(user.getEmail());

        assertAll(
                () -> assertThat(allByUserEmail).hasSize(3),
                () -> assertThat(allByUserEmail).contains(save, save1, save2),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.equals(save)), "testId")),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.equals(save1)), "testId2")),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.equals(save2)), "testId3")),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialId().equals("testId")), "testId")),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialId().equals("testId2")), "testId2")),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialId().equals("testId3")), "testId3")),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialType().equals(SocialType.KAKAO.getValue())), SocialType.KAKAO.getValue())),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialType().equals(SocialType.GOOGLE.getValue())), SocialType.GOOGLE.getValue())),
                () -> assertThat(allByUserEmail).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialType().equals(SocialType.NAVER.getValue())), SocialType.NAVER.getValue()))
        );

    }

    @Test
    void findSocialInfoBySocialId() {
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
        SocialInfo save = socialInfoRepository.save(socialInfoDto.toEntity());
        SocialInfo save1 = socialInfoRepository.save(socialInfoDto2.toEntity());
        SocialInfo save2 = socialInfoRepository.save(socialInfoDto3.toEntity());

        Optional<SocialInfo> test = socialInfoRepository.findSocialInfoBySocialId("testId");

        assertAll(
                () -> assertThat(test).hasValue(save).get().has(new Condition<>(s -> s.getSocialId().equals("testId"), "testId")),
                () -> assertThat(test).hasValue(save).get().has(new Condition<>(s -> s.getSocialType().equals(SocialType.KAKAO.getValue()), SocialType.KAKAO.getValue())),
                () -> assertThat(test).hasValue(save).get().is(new Condition<>(s -> s.getUser().equals(user), "test"))
        );
    }

    @DisplayName("회원 닉네임으로 모든 회원의 소셜정보를 가져온다.")
    @Test
    void findAllByUserNickName() {
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
        SocialInfo save = socialInfoRepository.save(socialInfoDto.toEntity());
        SocialInfo save1 = socialInfoRepository.save(socialInfoDto2.toEntity());
        SocialInfo save2 = socialInfoRepository.save(socialInfoDto3.toEntity());

        List<SocialInfo> allByUserNickName = socialInfoRepository.findAllByUserNickName(user.getNickName());

        assertAll(
                () -> assertThat(allByUserNickName).hasSize(3),
                () -> assertThat(allByUserNickName).contains(save, save1, save2),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.equals(save)), "testId")),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.equals(save1)), "testId2")),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.equals(save2)), "testId3")),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialId().equals("testId")), "testId")),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialId().equals("testId2")), "testId2")),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialId().equals("testId3")), "testId3")),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialType().equals(SocialType.KAKAO.getValue())), SocialType.KAKAO.getValue())),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialType().equals(SocialType.GOOGLE.getValue())), SocialType.GOOGLE.getValue())),
                () -> assertThat(allByUserNickName).has(new Condition<>(s -> s.stream().anyMatch(socialInfo -> socialInfo.getSocialType().equals(SocialType.NAVER.getValue())), SocialType.NAVER.getValue()))
        );
    }

    @DisplayName("회원 이메일로 모든 회원의 소셜정보를 삭제한다.")
    @Test
    void deleteAllByUserEmail() {
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
        SocialInfo save = socialInfoRepository.save(socialInfoDto.toEntity());
        SocialInfo save1 = socialInfoRepository.save(socialInfoDto2.toEntity());
        SocialInfo save2 = socialInfoRepository.save(socialInfoDto3.toEntity());

        socialInfoRepository.deleteAllByUserEmail(user.getEmail());

        List<SocialInfo> allByUserEmail = socialInfoRepository.findAllByUserEmail(user.getEmail());

        assertAll(
                () -> assertThat(allByUserEmail).isEmpty()
        );
    }
}