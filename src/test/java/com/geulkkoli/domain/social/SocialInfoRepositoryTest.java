package com.geulkkoli.domain.social;

import com.geulkkoli.application.social.util.SocialType;
import com.geulkkoli.domain.social.service.SocialInfo;
import com.geulkkoli.domain.social.service.SocialInfoRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class SocialInfoRepositoryTest {

    @Autowired
    private SocialInfoRepository socialInfoRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        socialInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
    @Test
    void socialInfoSave() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        userRepository.save(user);

        SocialInfo socialInfo = SocialInfo.builder()
                .user(user)
                .socialType(SocialType.KAKAO.getValue())
                .socialId("1234")
                .socialConnectDate(LocalDateTime.of(2021, 1, 1, 0, 0, 0).toString())
                .build();

        SocialInfo save = socialInfoRepository.save(socialInfo);

        Long socialInfoId = save.getSocialInfoId();
        assertThat(socialInfoId).isEqualTo(1L);
    }
}