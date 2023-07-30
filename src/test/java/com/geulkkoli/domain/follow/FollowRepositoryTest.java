package com.geulkkoli.domain.follow;

import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@DataJpaTest
class FollowRepositoryTest {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        followRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @DisplayName("팔로우 하는 사람 수 조회")
    @Test
    void countByFolloweeUserId() {
        User user = User.builder()
                .email("test@gmail.com")
                .nickName("test")
                .password("1234")
                .phoneNo("010-1111-2222")
                .gender("Male")
                .userName("tester1")
                .build();


        User user2 = User.builder()
                .email("test1@gmail.com")
                .nickName("test1")
                .password("1234")
                .phoneNo("010-2222-3333")
                .nickName("test1")
                .userName("tester2")
                .gender("female")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        Follow follow = user.follow(user2);
        followRepository.save(follow);

        assertThat(followRepository.countByFolloweeUserId(user2.getUserId())).isEqualTo(1);
    }

    @DisplayName("팔로워 하는 사람 수 조회")
    @Test
    void countByFollowerUserId() {
        User user = User.builder()
                .email("test@gmail.com")
                .nickName("test")
                .password("1234")
                .phoneNo("010-1111-2222")
                .gender("Male")
                .userName("tester1")
                .build();


        User user2 = User.builder()
                .email("test1@gmail.com")
                .nickName("test1")
                .password("1234")
                .phoneNo("010-2222-3333")
                .nickName("test1")
                .userName("tester2")
                .gender("female")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        Follow follow = user.follow(user2);
        followRepository.save(follow);

        assertThat(followRepository.countByFollowerUserId(user.getUserId())).isEqualTo(1);
    }

    @DisplayName("팔로우 아이디와 팔로이 아이디와 일치하는 모든 팔로우 조회")
    @Test
    void findAllByFolloweeUserIdOrFollowerUserId() {
        User user = User.builder()
                .email("test@gmail.com")
                .nickName("test")
                .password("1234")
                .phoneNo("010-1111-2222")
                .gender("Male")
                .userName("tester1")
                .build();


        User user2 = User.builder()
                .email("test1@gmail.com")
                .nickName("test1")
                .password("1234")
                .phoneNo("010-2222-3333")
                .nickName("test1")
                .userName("tester2")
                .gender("female")
                .build();

        User user3 = User.builder()
                .email("test2@gmail.com")
                .nickName("test2")
                .password("1234")
                .phoneNo("010-3333-4444")
                .nickName("test2")
                .gender("None")
                .userName("tester3")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Follow follow = user.follow(user2);
        Follow follow2 = user2.follow(user3);
        Follow follow3 = user3.follow(user);
        Follow follow4 = user3.follow(user2);

        followRepository.save(follow);
        followRepository.save(follow2);
        followRepository.save(follow3);
        followRepository.save(follow4);

        List<Follow> allByFolloweeUserIdOrFollowerUserId = followRepository.findAllByFolloweeUserIdOrFollowerUserId(user2.getUserId(), user2.getUserId());

        assertAll(
                () -> assertThat(allByFolloweeUserIdOrFollowerUserId).hasSize(3),
                () -> assertThat(allByFolloweeUserIdOrFollowerUserId).contains(follow, follow2, follow4)
          );
    }

    @DisplayName("팔로우 필로이 아이디로 팔로우 조회")
    @Test
    void findByFolloweeUserIdAndFollowerUserId() {
        User user = User.builder()
                .email("test@gmail.com")
                .nickName("test")
                .password("1234")
                .phoneNo("010-1111-2222")
                .gender("Male")
                .userName("tester1")
                .build();


        User user2 = User.builder()
                .email("test1@gmail.com")
                .nickName("test1")
                .password("1234")
                .phoneNo("010-2222-3333")
                .nickName("test1")
                .userName("tester2")
                .gender("female")
                .build();

        User user3 = User.builder()
                .email("test2@gmail.com")
                .nickName("test2")
                .password("1234")
                .phoneNo("010-3333-4444")
                .nickName("test2")
                .gender("None")
                .userName("tester3")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Follow follow = user.follow(user2);
        Follow follow2 = user2.follow(user3);
        Follow follow3 = user3.follow(user);
        Follow follow4 = user3.follow(user2);

        followRepository.save(follow);
        followRepository.save(follow2);
        followRepository.save(follow3);
        followRepository.save(follow4);

        Optional<Follow> findByFollow = followRepository.findByFolloweeUserIdAndFollowerUserId(user2.getUserId(), user.getUserId());

        assertAll(
                () -> assertThat(findByFollow).isNotEmpty(),
                () -> assertThat(findByFollow).hasValue(follow));
    }

    @DisplayName("팔로우 아이디 밎 팔로이 아이디로 팔로우 존재 여부 확인")
    @Test
    void existsByFolloweeUserIdAndFollowerUserId() {
        User user = User.builder()
                .email("test@gmail.com")
                .nickName("test")
                .password("1234")
                .phoneNo("010-1111-2222")
                .gender("Male")
                .userName("tester1")
                .build();


        User user2 = User.builder()
                .email("test1@gmail.com")
                .nickName("test1")
                .password("1234")
                .phoneNo("010-2222-3333")
                .nickName("test1")
                .userName("tester2")
                .gender("female")
                .build();

        User user3 = User.builder()
                .email("test2@gmail.com")
                .nickName("test2")
                .password("1234")
                .phoneNo("010-3333-4444")
                .nickName("test2")
                .gender("None")
                .userName("tester3")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Follow follow = user.follow(user2);
        Follow follow2 = user2.follow(user3);
        Follow follow3 = user3.follow(user);
        Follow follow4 = user3.follow(user2);

        followRepository.save(follow);
        followRepository.save(follow2);
        followRepository.save(follow3);
        followRepository.save(follow4);


        assertTrue(followRepository.existsByFolloweeUserIdAndFollowerUserId(user2.getUserId(), user.getUserId()));
        assertFalse(followRepository.existsByFolloweeUserIdAndFollowerUserId(user.getUserId(), user2.getUserId()));
    }
}