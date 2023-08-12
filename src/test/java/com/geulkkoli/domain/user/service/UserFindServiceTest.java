package com.geulkkoli.domain.user.service;

import com.geulkkoli.domain.user.User;
import com.geulkkoli.web.home.dto.JoinDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserFindServiceTest {
    @Autowired
    private UserFindService userFindService;
    @Autowired
    private UserService userService;

    @DisplayName("유저 아이디로 찾기")
    @Test
    void findById() {
        JoinDTO joinForm = JoinDTO.of("test","123","123","testName","test@email.com","01056789999","Male");
        User user = userService.signUp(joinForm);
        User findById = userFindService.findById(user.getUserId());

        assertEquals(user.getUserId(),findById.getUserId());
    }

    @DisplayName("유저 이메일로 찾기")
    @Test
    void findByEmail() {
        JoinDTO joinForm = JoinDTO.of("test","123","123","testName","test@email.com","01056789999","Male");
        User user = userService.signUp(joinForm);
        Optional<User> findByEmail = userFindService.findByEmail(user.getEmail());

        assertThat(findByEmail).hasValue(user);
    }

    @DisplayName("유저 닉네임, 전화번호로 찾기")
    @Test
    void findByUserNameAndPhoneNo() {
        JoinDTO joinForm = JoinDTO.of("test","123","123","testName","test@email.com","01056789999","Male");
        User user = userService.signUp(joinForm);
        Optional<User> findByUserNameAndPhoneNo = userFindService.findByUserNameAndPhoneNo(user.getUserName(),user.getPhoneNo());

        assertThat(findByUserNameAndPhoneNo).hasValue(user);
    }

    @DisplayName("유저 이메일, 닉네임, 전화번호로 찾기")
    @Test
    void findByEmailAndUserNameAndPhoneNo() {
        JoinDTO joinForm = JoinDTO.of("test","123","123","testName","test@email.com","01056789999","Male");

        User user = userService.signUp(joinForm);
        Optional<User> findByEmailAndUserNameAndPhoneNo = userFindService.findByEmailAndUserNameAndPhoneNo(user.getEmail(),user.getUserName(),user.getPhoneNo());

        assertThat(findByEmailAndUserNameAndPhoneNo).hasValue(user);
    }

    @DisplayName("유저 닉네임으로 찾기")
    @Test
    void findByNickName() {
        JoinDTO joinForm = JoinDTO.of("test","123","123","testName","test@email.com","01056789999","Male");

        User user = userService.signUp(joinForm);
        User findByNickName = userFindService.findByNickName(user.getNickName());

        assertEquals(user.getNickName(),findByNickName.getNickName());
    }

    @Test
    @DisplayName("이메일 중복")
    void isEmailDuplicate() {

        JoinDTO joinForm = JoinDTO.of("test","123","123","testName","tako1@naver.com","01056789999","Male");

        User user = userService.signUp(joinForm);
        assertThat(userFindService.isEmailDuplicate("tako1@naver.com")).isTrue();
    }

    @Test
    @DisplayName("별명 중복")
    void isNickNameDuplicate() {
        JoinDTO joinForm = JoinDTO.of("test","123","123","바나나1","tako1@naver.com","01056789999","Male");

        User user = userService.signUp(joinForm);
        assertThat(userFindService.isNickNameDuplicate("바나나1")).isTrue();
    }

    @Test
    @DisplayName("전화번호 중복확인")
    void isPhoneNoDuplicate() {
        JoinDTO joinForm = JoinDTO.of("test","123","123","바나나1","tako1@naver.com","01012345671","Male");

        User user = userService.signUp(joinForm);
        assertThat(userFindService.isPhoneNoDuplicate("01012345671")).isTrue();
    }
}