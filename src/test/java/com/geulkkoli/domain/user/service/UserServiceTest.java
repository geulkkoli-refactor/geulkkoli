package com.geulkkoli.domain.user.service;

import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.account.dto.edit.PasswordEditFormDto;
import com.geulkkoli.web.home.dto.JoinDTO;
import com.geulkkoli.web.account.dto.edit.UserInfoEditFormDto;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void updateTest() {
        //given
        JoinDTO joinForm = JoinDTO.of("test", "123", "123", "nick", "test12222@naver.com", "01056789999", "Male");

        //when
        User saveUser = userService.signUp(joinForm);
        UserInfoEditFormDto preupdateUser = UserInfoEditFormDto.form("김2", "바나나155", "01055554646", "female");

        //when
        userService.edit(saveUser.getUserId(), preupdateUser);
        Optional<User> one = userRepository.findById(saveUser.getUserId());

        // then
        assertThat(one).get().extracting("userName", "nickName", "phoneNo", "gender").containsExactly("김2", "바나나155", "01055554646", "female");
    }

    @DisplayName("회원가입")
    @Test
    void signUp() {
        //given
        JoinDTO joinForm = JoinDTO.of("test", "123", "123", "nick", "test12222@naver.com", "01056789999", "Male");

        //when
        User user = userService.signUp(joinForm);

        //then
        assertThat(user).has(new Condition<>(u -> u.getEmail().equals("test12222@naver.com"), "이메일이 비어있지 않다"));
    }

    @DisplayName("회원탈퇴")
    @Test
    void delete() {
        //given
        JoinDTO joinForm = JoinDTO.of("test", "123", "123", "nick", "test1234111@naver.com", "01056789999", "Male");
        User user = userService.signUp(joinForm);

        //when
        userService.delete(user);

        //then
        assertThat(userRepository.findById(user.getUserId())).isEmpty();
    }

    @DisplayName("비밀번호 검증")
    @Test
    void isPasswordVerification() {
        //given
        JoinDTO joinForm = JoinDTO.of("test", "123", "123", "nick", "test1234111@naver.com", "01056789999", "Male");
        User user = userService.signUp(joinForm);
        PasswordEditFormDto passwordEditFormDto = new PasswordEditFormDto("123", "qwerty1@", "qwerty1@");

        //when
        boolean passwordVerification = userService.isPasswordVerification(user.getPassword(), passwordEditFormDto.getOldPassword());

        //then
        assertThat(passwordVerification).isTrue();

    }

    @DisplayName("비밀번호 수정")
    @Test
    void updatePassword() {
        //given
        JoinDTO joinForm = JoinDTO.of("test", "123", "123", "nick", "test1234111@naver.com", "01056789999", "Male");
        User user = userService.signUp(joinForm);
        PasswordEditFormDto passwordEditFormDto = new PasswordEditFormDto("123", "qwerty1@", "qwerty1@");

        //when
        User userByEditedPassword = userService.updatePassword(user.getUserId(), passwordEditFormDto.getNewPassword());

        //then
        assertTrue(userService.isPasswordVerification(userByEditedPassword, passwordEditFormDto));
    }
}
