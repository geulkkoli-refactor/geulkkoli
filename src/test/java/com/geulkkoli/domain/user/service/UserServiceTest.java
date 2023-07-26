package com.geulkkoli.domain.user.service;

import com.geulkkoli.application.user.service.PasswordService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.user.dto.JoinFormDto;
import com.geulkkoli.web.user.dto.edit.UserInfoEditFormDto;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    User saveUser;


    @BeforeAll
    void init() {
        saveUser = userRepository.save(User.builder() // userId = 3L
                .email("tako1@naver.com").userName("김1").nickName("바나나1").password("123qwe!@#").phoneNo("01012345671").gender("male").build());
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void updateTest() {
        //given
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
        JoinFormDto joinForm = JoinFormDto.of("test", "123", "123", "nick", "test@naver.com", "01056789999", "Male");

        //when
        User user = userService.signUp(joinForm);

        //then
        assertThat(user).hasFieldOrPropertyWithValue("userName", "test");
        assertThat(user).hasFieldOrPropertyWithValue("nickName", "nick");
        assertThat(user).hasFieldOrPropertyWithValue("email", "test@naver.com");
    }

    @DisplayName("회원탈퇴")
    @Test
    void delete() {
        //given
        JoinFormDto joinForm = JoinFormDto.of("test", "123", "123", "nick", "test@naver.com", "01056789999", "Male");
        User user = userService.signUp(joinForm);

        //when
        userService.delete(user);

        //then
        assertThat(userRepository.findById(user.getUserId())).isEmpty();
    }
}
