package com.geulkkoli.domain.user;

import com.geulkkoli.exception.EmptyDataException;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 저장 테스트")
    @Test
    void save() {
        User user = User.builder()
                .email("tako@naver1.com")
                .userName("김1")
                .nickName("바나나1")
                .password("12341")
                .gender("male1")
                .phoneNo("01012345679")
                .build();

        User saveUser = userRepository.save(user);

        assertThat(saveUser).isEqualTo(user);
    }

    @Test
    void findById() {
        User user = User.builder()
                .email("tako@naver1.com")
                .userName("김1")
                .nickName("바나나1")
                .password("12341")
                .gender("male1")
                .phoneNo("01012345679")
                .build();

        User save = userRepository.save(user);

        Optional<User> findUser = userRepository.findById(save.getUserId());

        assertAll(() -> assertThat(findUser).hasValue(save).get().has(new Condition<>(u -> Objects.equals(u.getUserId(), save.getUserId()), "id가 1인지 확인")),
                () -> assertThat(findUser).hasValue(save).get().has(new Condition<>(u -> u.getEmail().equals("tako@naver1.com"), "이메일 확인")));
    }

    @Test
    @DisplayName("이메일로 회원 조회")
    void findByEmail() {
        User user = User.builder()
                .email("tako@naver.com")
                .userName("김1")
                .nickName("바나나1")
                .password("12341")
                .gender("male1")
                .phoneNo("01012345679")
                .build();

        userRepository.save(user);
        User findByEmailUser = userRepository.findByEmail("tako@naver.com")
                .orElseThrow(() -> new EmptyDataException("해당 데이터가 존재하지 않습니다."));

        assertThat(findByEmailUser.getEmail()).isEqualTo("tako@naver.com");
    }

    @Test
    @DisplayName("닉네임으로 회원 조회")
    void findByNickName() {
        User user = User.builder()
                .email("tako1@naver.com")
                .userName("김1")
                .nickName("바나나1")
                .password("12341")
                .gender("male1")
                .phoneNo("01012345679")
                .build();

        userRepository.save(user);
        User findByNickNameUser = userRepository.findByNickName("바나나1")
                .orElseThrow(() -> new EmptyDataException("해당 데이터가 존재하지 않습니다."));

        assertThat(findByNickNameUser.getEmail()).isEqualTo("tako1@naver.com");
    }

    @Test
    @DisplayName("전화번호로 회원 조회")
    void findByPhoneNo() {
        User user = User.builder()
                .email("tako1@naver.com")
                .userName("김1")
                .nickName("바나나1")
                .password("12341")
                .gender("male1")
                .phoneNo("01012345679")
                .build();

        User saveUser = userRepository.save(user);

        assertThat(userRepository.findByPhoneNo(("01012345679"))).hasValue(saveUser);
    }

    @DisplayName("이메일, 닉네임, 전화번호로 회원 조회")
    @Test
    void findByUserNameAndPhoneNo() {
        User user = User.builder()
                .email("tako1@naver.com")
                .userName("김1")
                .nickName("바나나1")
                .password("12341")
                .gender("male1")
                .phoneNo("01012345679")
                .build();

        User saveUser = userRepository.save(user);

        Optional<User> findUser = userRepository.findByUserNameAndPhoneNo("김1", "01012345679");

        assertAll(() -> assertThat(findUser).hasValue(saveUser).get().has(new Condition<>(u -> u.getUserName().equals("김1"), "이름 확인")),
                () -> assertThat(findUser).hasValue(saveUser).get().has(new Condition<>(u -> u.getPhoneNo().equals("01012345679"), "전화번호 확인")));
    }

    @Test
    void findByEmailAndUserNameAndPhoneNo() {
        User user = User.builder()
                .email("tako1@naver.com")
                .userName("김1")
                .nickName("바나나1")
                .password("12341")
                .gender("male1")
                .phoneNo("01012345679")
                .build();

        User saveUser = userRepository.save(user);

        Optional<User> findUser = userRepository.findByEmailAndUserNameAndPhoneNo("tako1@naver.com", "김1", "01012345679");

        assertAll(() -> assertThat(findUser).hasValue(saveUser).get().has(new Condition<>(u -> u.getUserName().equals("김1"), "이름 확인")),
                () -> assertThat(findUser).hasValue(saveUser).get().has(new Condition<>(u -> u.getPhoneNo().equals("01012345679"), "전화번호 확인")),
                () -> assertThat(findUser).hasValue(saveUser).get().has(new Condition<>(u -> u.getEmail().equals("tako1@naver.com"), "이메일 확인")));
    }

}