package com.geulkkoli.web.user.social;

import com.geulkkoli.application.security.AccountStatus;
import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.application.user.UserModelDto;
import com.geulkkoli.web.social.SocialController;
import com.geulkkoli.web.social.SocialSignUpDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SocialControllerTest {


    @Autowired
    private SocialController socialController;

    @Test
    void if_NonExisting_Member_Go_To_SignUpPage() {

        UserModelDto userModelDto = UserModelDto.builder()
                .authorizaionServerId("test")
                .email("email")
                .gender("M")
                .phoneNo("010-1234-5678")
                .password("test")
                .build();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.GUEST.getRoleName()));
        CustomAuthenticationPrinciple authUser = CustomAuthenticationPrinciple.from(userModelDto, authorities, AccountStatus.ACTIVE);

        SocialSignUpDto socialDto = SocialSignUpDto.builder()
                .nickName("test")
                .password("test")
                .phoneNo("010-1234-5678")
                .email("test@gmail.com")
                .gender("M")
                .verifyPassword("test")
                .build();
        ModelAndView modelAndView = socialController.moveSignUpPage(authUser, new ModelAndView());

        assertThat(modelAndView.getViewName()).isEqualTo("social/oauth2/signup");
    }

    @Test
    void non_Existing_Member_SignUp_Success() {

        SocialSignUpDto socialDto = SocialSignUpDto.builder()
                .nickName("test")
                .password("test")
                .email("test1@gmail.com")
                .userName("test1")
                .verifyPassword("test")
                .phoneNo("01012345678")
                .gender("M")
                .build();


        BindingResult bindingResult = new BeanPropertyBindingResult(socialDto, "socialDto");
        ModelAndView modelAndView = socialController.signUp(socialDto, bindingResult, new ModelAndView());

        assertThat(modelAndView.getViewName()).isEqualTo("/home");
    }
}
