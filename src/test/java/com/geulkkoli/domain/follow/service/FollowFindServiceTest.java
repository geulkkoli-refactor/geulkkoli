package com.geulkkoli.domain.follow.service;

import com.geulkkoli.application.follow.FollowInfo;
import com.geulkkoli.application.follow.FollowInfos;
import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.security.RoleEntity;
import com.geulkkoli.application.security.RoleRepository;
import com.geulkkoli.domain.follow.Follow;
import com.geulkkoli.domain.follow.FollowRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class FollowFindServiceTest {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FollowFindService followFindService;

    @BeforeEach
    void setUp() {
        followRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void findSomeFolloweeByFollowerId() {
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

        User user3 = User.builder()
                .email("test3@gmail.com")
                .userName("test3").nickName("nickName3")
                .phoneNo("010-1112-1111")
                .gender("None")
                .password("1234")
                .build();

        User user4 = User.builder()
                .email("test4@gmail.com")
                .userName("test4").nickName("nickName4")
                .phoneNo("010-1113-1111")
                .gender("None")
                .password("1234")
                .build();

        User user5 = User.builder()
                .email("test5@gmail.com")
                .userName("test5").nickName("nickName5")
                .phoneNo("010-1114-1111")
                .gender("None")
                .password("1234")
                .build();

        User user6 = User.builder()
                .email("test6@gmail.com")
                .userName("test6").nickName("nickName6")
                .phoneNo("010-1115-1111")
                .gender("None")
                .password("1234")
                .build();

        RoleEntity roleEntity = user.addRole(Role.USER);
        RoleEntity roleEntity2 = user2.addRole(Role.USER);
        RoleEntity roleEntity3 = user3.addRole(Role.USER);
        RoleEntity roleEntity4 = user4.addRole(Role.USER);
        RoleEntity roleEntity5 = user5.addRole(Role.USER);
        RoleEntity roleEntity6 = user6.addRole(Role.USER);

        roleRepository.save(roleEntity);
        roleRepository.save(roleEntity2);
        roleRepository.save(roleEntity3);
        roleRepository.save(roleEntity4);
        roleRepository.save(roleEntity5);
        roleRepository.save(roleEntity6);

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);

        Follow followEntity = Follow.of(user2, user);
        Follow followEntity2 = Follow.of(user3, user);
        Follow followEntity3 = Follow.of(user4, user);
        Follow followEntity4 = Follow.of(user5, user);

        followRepository.save(followEntity);
        followRepository.save(followEntity2);
        followRepository.save(followEntity3);
        followRepository.save(followEntity4);

        FollowInfos someFolloweeByFollowerId = followFindService.findSomeFolloweeByFollowerId(user.getUserId(), followEntity2.getId(), Pageable.ofSize(3));

        assertThat(someFolloweeByFollowerId.followInfos()).hasSize(1);
    }


    @Test
    void findSomeFollowerByFolloweeId() {
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

        User user3 = User.builder()
                .email("test3@gmail.com")
                .userName("test3").nickName("nickName3")
                .phoneNo("010-1112-1111")
                .gender("None")
                .password("1234")
                .build();

        User user4 = User.builder()
                .email("test4@gmail.com")
                .userName("test4").nickName("nickName4")
                .phoneNo("010-1113-1111")
                .gender("None")
                .password("1234")
                .build();

        User user5 = User.builder()
                .email("test5@gmail.com")
                .userName("test5").nickName("nickName5")
                .phoneNo("010-1114-1111")
                .gender("None")
                .password("1234")
                .build();

        User user6 = User.builder()
                .email("test6@gmail.com")
                .userName("test6").nickName("nickName6")
                .phoneNo("010-1115-1111")
                .gender("None")
                .password("1234")
                .build();

        RoleEntity roleEntity = user.addRole(Role.USER);
        RoleEntity roleEntity2 = user2.addRole(Role.USER);
        RoleEntity roleEntity3 = user3.addRole(Role.USER);
        RoleEntity roleEntity4 = user4.addRole(Role.USER);
        RoleEntity roleEntity5 = user5.addRole(Role.USER);
        RoleEntity roleEntity6 = user6.addRole(Role.USER);

        roleRepository.save(roleEntity);
        roleRepository.save(roleEntity2);
        roleRepository.save(roleEntity3);
        roleRepository.save(roleEntity4);
        roleRepository.save(roleEntity5);
        roleRepository.save(roleEntity6);

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);

        Follow followEntity = Follow.of(user, user2);
        Follow followEntity2 = Follow.of(user, user3);
        Follow followEntity3 = Follow.of(user, user4);
        Follow followEntity4 = Follow.of(user, user5);

        followRepository.save(followEntity);
        followRepository.save(followEntity2);
        followRepository.save(followEntity3);
        followRepository.save(followEntity4);

        FollowInfos someFollowerByFolloweeId = followFindService.findSomeFollowerByFolloweeId(user.getUserId(), followEntity2.getId(), Pageable.ofSize(3));

        assertThat(someFollowerByFolloweeId.getFollowInfos()).hasSize(1);


    }

    @DisplayName("나를 팔로우하는 사람 중에 내가 팔로우하는 사람 찾기")
    @Test
    void findUserIdByFollowedEachOther() {
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

        User user3 = User.builder()
                .email("test3@gmail.com")
                .userName("test3").nickName("nickName3")
                .phoneNo("010-1112-1111")
                .gender("None")
                .password("1234")
                .build();

        User user4 = User.builder()
                .email("test4@gmail.com")
                .userName("test4").nickName("nickName4")
                .phoneNo("010-1113-1111")
                .gender("None")
                .password("1234")
                .build();

        User user5 = User.builder()
                .email("test5@gmail.com")
                .userName("test5").nickName("nickName5")
                .phoneNo("010-1114-1111")
                .gender("None")
                .password("1234")
                .build();

        User user6 = User.builder()
                .email("test6@gmail.com")
                .userName("test6").nickName("nickName6")
                .phoneNo("010-1115-1111")
                .gender("None")
                .password("1234")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);

        followRepository.save(user.follow(user2));
        followRepository.save(user.follow(user3));
        followRepository.save(user2.follow(user));
        followRepository.save(user3.follow(user));
        followRepository.save(user4.follow(user));
        followRepository.save(user5.follow(user));
        followRepository.save(user6.follow(user));

        List<Long> userIds = followFindService.findSomeFollowerByFolloweeId(user.getUserId(), null, Pageable.ofSize(5)).getFollowInfos()
                .stream()
                .map(FollowInfo::getUserId)
                .collect(Collectors.toList());

        List<Long> userIdByFollowedEachOther = followFindService.findUserIdByFollowedEachOther(userIds, user.getUserId(), 3);

        assertAll(
                () -> assertThat(userIds).hasSize(5),
                () -> assertThat(userIdByFollowedEachOther).hasSize(2),
                () -> assertThat(userIdByFollowedEachOther).contains(user2.getUserId(), user3.getUserId())
        );

    }

    @DisplayName("팔로이 아이디로 팔로우 수 조회")
    @Test
    void countFollowerByFolloweeId() {
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

        User user3 = User.builder()
                .email("test3@gmail.com")
                .userName("test3").nickName("nickName3")
                .phoneNo("010-1112-1111")
                .gender("None")
                .password("1234")
                .build();

        User user4 = User.builder()
                .email("test4@gmail.com")
                .userName("test4").nickName("nickName4")
                .phoneNo("010-1113-1111")
                .gender("None")
                .password("1234")
                .build();

        User user5 = User.builder()
                .email("test5@gmail.com")
                .userName("test5").nickName("nickName5")
                .phoneNo("010-1114-1111")
                .gender("None")
                .password("1234")
                .build();

        User user6 = User.builder()
                .email("test6@gmail.com")
                .userName("test6").nickName("nickName6")
                .phoneNo("010-1115-1111")
                .gender("None")
                .password("1234")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);

        followRepository.save(user.follow(user2));
        followRepository.save(user.follow(user3));
        followRepository.save(user2.follow(user));
        followRepository.save(user3.follow(user));
        followRepository.save(user4.follow(user));
        followRepository.save(user5.follow(user));
        followRepository.save(user6.follow(user));

        Integer count = followFindService.countFollowerByFolloweeId(user.getUserId());

        assertThat(count).isEqualTo(5);
    }

    @DisplayName("팔로워 아이디로 팔로이 수 조회")
    @Test
    void countFolloweeByFollowerId() {
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

        User user3 = User.builder()
                .email("test3@gmail.com")
                .userName("test3").nickName("nickName3")
                .phoneNo("010-1112-1111")
                .gender("None")
                .password("1234")
                .build();

        User user4 = User.builder()
                .email("test4@gmail.com")
                .userName("test4").nickName("nickName4")
                .phoneNo("010-1113-1111")
                .gender("None")
                .password("1234")
                .build();

        User user5 = User.builder()
                .email("test5@gmail.com")
                .userName("test5").nickName("nickName5")
                .phoneNo("010-1114-1111")
                .gender("None")
                .password("1234")
                .build();

        User user6 = User.builder()
                .email("test6@gmail.com")
                .userName("test6").nickName("nickName6")
                .phoneNo("010-1115-1111")
                .gender("None")
                .password("1234")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);

        followRepository.save(user.follow(user2));
        followRepository.save(user.follow(user3));
        followRepository.save(user2.follow(user));
        followRepository.save(user3.follow(user));
        followRepository.save(user4.follow(user));
        followRepository.save(user5.follow(user));
        followRepository.save(user6.follow(user));

        Integer count = followFindService.countFolloweeByFollowerId(user.getUserId());

        assertThat(count).isEqualTo(2);
    }

    @DisplayName("팔로우 여부 확인")
    @Test
    void checkFollow() {
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

        User user3 = User.builder()
                .email("test3@gmail.com")
                .userName("test3").nickName("nickName3")
                .phoneNo("010-1112-1111")
                .gender("None")
                .password("1234")
                .build();

        User user4 = User.builder()
                .email("test4@gmail.com")
                .userName("test4").nickName("nickName4")
                .phoneNo("010-1113-1111")
                .gender("None")
                .password("1234")
                .build();

        User user5 = User.builder()
                .email("test5@gmail.com")
                .userName("test5").nickName("nickName5")
                .phoneNo("010-1114-1111")
                .gender("None")
                .password("1234")
                .build();

        User user6 = User.builder()
                .email("test6@gmail.com")
                .userName("test6").nickName("nickName6")
                .phoneNo("010-1115-1111")
                .gender("None")
                .password("1234")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);

        followRepository.save(user.follow(user2));
        followRepository.save(user.follow(user3));
        followRepository.save(user2.follow(user));
        followRepository.save(user3.follow(user));
        followRepository.save(user4.follow(user));
        followRepository.save(user5.follow(user));
        followRepository.save(user6.follow(user));

        Boolean check = followFindService.checkFollow(user, user2);

        assertThat(check).isTrue();
    }
}