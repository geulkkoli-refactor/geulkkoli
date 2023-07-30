package com.geulkkoli.domain.admin;

import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@DataJpaTest
class AccountRockRepositoryTest {

    @Autowired
    private AccountLockRepository accountRockRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void teardown() {
        accountRockRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }



    @DisplayName("정지 처분된 유저가 아이디로 찾기")
    @Test
    void findById() {
        //given

        User user = User.builder()
                .email("test121@gmail.com")
                .userName("name121")
                .nickName("nickName")
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user);

        User user2 = User.builder()
                .email("test145@gmail.com")
                .userName("name121")
                .nickName("33231")
                .phoneNo("010-12111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user2);



        Optional<User> byId = userRepository.findById(user2.getUserId());
        AccountLock accountLock = AccountLock.of(byId.get(), "정지사유", LocalDateTime.of(2023, 5, 11, 22, 22, 22));

        AccountLock save = accountRockRepository.save(accountLock);
        //then
        Optional<AccountLock> lockedUser = accountRockRepository.findByUserId(user2.getUserId());

        assertThat(lockedUser).isIn(Optional.of(save)).hasValue(save);
        assertAll(
                () -> assertThat(lockedUser).isIn(Optional.of(save)),
                () -> assertThat(lockedUser).hasValue(save));
    }

    @DisplayName("정지 처분된 유저가 존재하는지 확인")
    @Test
    void existsById() {
        //given
        User user = User.builder()
                .email("test121@gmail.com")
                .userName("name121")
                .nickName("nickName")
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user);

        User user2 = User.builder()
                .email("test145@gmail.com")
                .userName("name121")
                .nickName("33231")
                .phoneNo("010-12111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user2);

        Optional<User> byId = userRepository.findById(user2.getUserId());
        AccountLock accountLock = AccountLock.of(byId.get(), "정지사유", LocalDateTime.of(2023, 5, 11, 22, 22, 22));

        AccountLock save = accountRockRepository.save(accountLock);
        //then
        Boolean aBoolean = accountRockRepository.existsByLockedUser_UserId(save.getLockedUser().getUserId());

        assertThat(aBoolean).isTrue();
    }
}