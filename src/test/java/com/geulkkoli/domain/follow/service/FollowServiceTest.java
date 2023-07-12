package com.geulkkoli.domain.follow.service;

import com.geulkkoli.domain.follow.Follow;
import com.geulkkoli.domain.follow.FollowRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class FollowServiceTest {
    @Autowired
    FollowRepository followRepository;
    @Autowired
    FollowService followService;
    @Autowired
    FollowFindService followFindService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        followRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    void follow() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .gender("Male")
                .password("1234")
                .nickName("nickName")
                .phoneNo("010-1234-1234")
                .build();
        User user2 = User.builder()
                .email("test2@gmail.com")
                .userName("test2").nickName("nickName2")
                .phoneNo("010-1111-1111")
                .gender("None")
                .password("1234")
                .build();

        userRepository.save(user);
        userRepository.save(user2);


        Follow followEntity = Follow.of(user, user2);

        followService.follow(user, user2);

        assertAll(() -> {
            assertThat(followEntity.getFollowee()).isEqualTo(user);
            assertThat(followEntity.getFollower()).isEqualTo(user2);
            assertThat(followEntity.getFollowee().getNickName()).isEqualTo("nickName");
            assertThat(followEntity.getFollower().getNickName()).isEqualTo("nickName2");
        });
    }

    @DisplayName("스스로를 팔로우하면 예외를 반환한다")
    @Test
    void if_follow_self_throw_exception() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .gender("Male")
                .password("1234")
                .nickName("nickName")
                .phoneNo("010-1234-1234")
                .build();

        User saveUser = userRepository.save(user);

        assertThrows(CanNotFollowException.class, () ->
                followService.follow(saveUser, saveUser));
    }


    @DisplayName("언 팔로우")
    @Test
    void unfollow() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .gender("Male")
                .password("1234")
                .nickName("nickName")
                .phoneNo("010-1234-1234")
                .build();
        User user2 = User.builder()
                .email("test2@gmail.com")
                .userName("test2").nickName("nickName2")
                .phoneNo("010-1111-1111")
                .gender("None")
                .password("1234")
                .build();
        User save = userRepository.save(user);
        User save1 = userRepository.save(user2);

        followService.follow(user, user2);
        Follow follow = followService.unFollow(save, save1);

        assertAll(() -> {
            assertThat(follow.getFollowee()).isEqualTo(save);
            assertThat(follow.getFollower()).isEqualTo(save1);
            assertThat(follow.getFollowee().getNickName()).isEqualTo("nickName");
            assertThat(follow.getFollower().getNickName()).isEqualTo("nickName2");
        });
    }

    @DisplayName("존재하지 않는 팔로우를 언팔로우하면 예외를 반환한다")
    @Test
    void if_unfollow_not_exist_follow_throw_exception() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .gender("Male")
                .password("1234")
                .nickName("nickName")
                .phoneNo("010-1234-1234")
                .build();
        User user2 = User.builder()
                .email("test2@gmail.com")
                .userName("test2").nickName("nickName2")
                .phoneNo("010-1111-1111")
                .gender("None")
                .password("1234")
                .build();
        User save = userRepository.save(user);
        User save1 = userRepository.save(user2);

        assertThrows(FollowNotFoundException.class, () ->
                followService.unFollow(save, save1));}

    @DisplayName("모든 팔로우를 언팔로우한다")
    @Test
    void allUnfollow() {
        User user = User.builder()
                .email("test@gmail.com")
                .userName("test")
                .gender("Male")
                .password("1234")
                .nickName("nickName")
                .phoneNo("010-1234-1234")
                .build();
        User user2 = User.builder()
                .email("test2@gmail.com")
                .userName("test2").nickName("nickName2")
                .phoneNo("010-1111-1111")
                .gender("None")
                .password("1234")
                .build();

        User user3= User.builder()
                .email("test3@gmail.com")
                .userName("test3").nickName("nickName3")
                .phoneNo("01044445555")
                .gender("None")
                .password("1234")
                .build();

        User save = userRepository.save(user);
        User save1 = userRepository.save(user2);
        User save2 = userRepository.save(user3);

        followService.follow(save, save1);
        followService.follow(save, save2);

        followService.allUnfollow(save);

        assertAll(() -> {
            assertThat(followFindService.countFolloweeByFollowerId(save.getUserId())).isZero();
            assertThat(followFindService.countFolloweeByFollowerId(save.getUserId())).isZero();
        });

    }
}