package com.geulkkoli.application.security;

import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.application.user.service.PasswordService;
import com.geulkkoli.application.user.service.UserSecurityService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.domain.user.service.UserService;
import com.geulkkoli.web.home.dto.JoinFormDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserSecurityServiceTest {

    @Autowired
    UserSecurityService userSecurityService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordService passwordService;
    @Autowired
    UserFindService userFindService;


    @Test
    @DisplayName("가입 시 성공시 유저 권한 부여 테스트")
    void join() {
        JoinFormDto joinForm =JoinFormDto.of("test","123","123","nick","test@naver.com","01056789999","Male");
        User user = userService.signUp(joinForm);

        assertThat(user.isUser()).isTrue();
    }

    @Test
    @DisplayName("회원 권한을 가진 유저가 올바르게 로그인 했을 때 회원 권한 인증 정보를 반환한다.")
    void Returns_membership_credentials_when_a_user_with_membership_privileges_is_correctly_logged_in() {
        JoinFormDto joinForm =JoinFormDto.of("test","123","123","nick","test@naver.com","01056789999","Male");

        User saveUser = userService.signUp(joinForm);

        //when
        UserDetails user = userSecurityService.loadUserByUsername("test@naver.com");
        CustomAuthenticationPrinciple authUser = (CustomAuthenticationPrinciple) user;
        //then

        assertAll(() -> assertThat(authUser.getAuthorities()).hasSize(1),
                () -> assertThat(Objects.requireNonNull(authUser.getAuthorities()).iterator().next().getAuthority()).isEqualTo("ROLE_USER"),
                () -> assertThat(authUser.getUsername()).isEqualTo("test@naver.com"),
                () -> assertThat(authUser.getPassword()).isEqualTo(saveUser.getPassword()),
                () -> assertThat(authUser.isAccountNonExpired()).isTrue(),
                () -> assertThat(authUser.isAccountNonLocked()).isTrue(),
                () -> assertThat(authUser.isCredentialsNonExpired()).isTrue(),
                () -> assertThat(authUser.isEnabled()).isTrue());
    }

    @Test
    @DisplayName("존재하지 않는 이메일일 경우 usernameNotFoundException 예외가 발생한다.")
    void exception_thrown_for_non_existent_emails() {

        assertThrows(UsernameNotFoundException.class, () -> userSecurityService.loadUserByUsername("tako11@naver.com"));

    }

}