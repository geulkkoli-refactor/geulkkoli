package com.geulkkoli.web.user;

import com.geulkkoli.application.user.service.UserSecurityService;
import com.geulkkoli.application.security.config.SecurityConfig;
import com.geulkkoli.application.security.handler.LoginFailureHandler;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


/*
    MockMvc는 가상의 클라이언트를 생성해주는 것과 비슷하게 사용 가능한데,
    파라미터를 맵으로 선언해 넣어주는 방법과 일일이 .params로 지정해주는 방법이 있다.
    아래 테스트에서는 요청 시 요청 사항들을 보다 간략하고 보기 쉽게 하게 위해 대부분 맵을 사용하였다.


    WebMvcTest로 변경함에 따라 SpringBootTest에서 불러왔던 여러 컴포넌트를 불러올 수 없게 되므로
    MockBean으로 가상의 빈 객체를 쓰겠다고 선언해 준 이후,
    given으로 해당 메서드의 파라미터로 뭔가 넣었을 때 return될 값을 정해준다.
 */
@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, LoginFailureHandler.class, UserSecurityService.class})
class UserControllerTest {
    public static final String TESTER_MAIL = "tester@naver.com";
    public static final String TESTER_PASSWORD = "qwe123!@";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;
//    @MockBean
//    private UserSecurityService userSecurityService;

    User user;

    @BeforeEach
    public void init() {
        user = User.builder()
                .email(TESTER_MAIL)
                .userName("유저이름1")
                .nickName("유저닉1")
                .password(passwordEncoder.encode(TESTER_PASSWORD))
                .phoneNo("01012345678")
                .gender("male")
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 잘 저장 되는지 테스트")
    void joinUserTest() throws Exception {
        //given
        User joinUser = User.builder()
                .userName("fishs")
                .password("qwe123!@")
                .nickName("takos")
                .email("takodachi@naver.com")
                .phoneNo("01071397733")
                .gender("male")
                .build();

        given(userRepository.findByEmail(any())).willReturn(Optional.ofNullable(joinUser));

        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("userName", "fishs");
        query_param.add("password", "qwe123!@");
        query_param.add("verifyPassword", "1234WnRnal@");
        query_param.add("nickName", "takos");
        query_param.add("email", "takodachi@naver.com");
        query_param.add("phoneNo", "01071397733");
        query_param.add("gender", "male");

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/join")
                .params(query_param));

        User findByLoginIdUser = userRepository.findByEmail("takodachi@naver.com").
                orElse(null);

        //then
        assertThat(findByLoginIdUser).isNotNull();
        assertThat(findByLoginIdUser.getGender()).isEqualTo("male");
    }

    @Test
    @DisplayName("회원가입 이메일 중복체크 되는지 테스트")
    void joinEmailDupleTest() throws Exception {
        //given
        given(userRepository.findByEmail("tako@naver.com")).willReturn(Optional.ofNullable(user));
        given(userService.isEmailDuplicate(TESTER_MAIL)).willReturn(true);
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("userName", "fish");
        query_param.add("password", "1234WnRnal@");
        query_param.add("verifyPassword", "1234WnRnal@");
        query_param.add("nickName", "타코메일");
        query_param.add("email", TESTER_MAIL);
        query_param.add("phoneNo", "01071397733");
        query_param.add("gender", "male");

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/join")
                        .params(query_param)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        User findByEmailIdUser = userRepository.findByEmail("tako@naver.com").
                orElse(null);

        //then
        assertThat(findByEmailIdUser).isNotNull();
        assertThat(findByEmailIdUser.getUserName()).isEqualTo("유저이름1");
        assertThat(findByEmailIdUser.getUserName()).doesNotContain("fish");
    }

    @Test
    @DisplayName("회원가입 닉네임 중복 체크 되는지 테스트")
    void joinNickNameDupleTest() throws Exception {
        given(userService.isNickNameDuplicate("유저닉1")).willReturn(true);

        //given
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("userName", "fish");
        query_param.add("password", "WnRnal");
        query_param.add("verifyPassword", "1234WnRnal@");
        query_param.add("nickName", "유저닉1");
        query_param.add("email", "takoNickTest@naver.com");
        query_param.add("phoneNo", "01071397733");
        query_param.add("gender", "male");

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/join")
                        .params(query_param)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        User findByEmailIdUser = userRepository.findByEmail("takoNickTest@naver.com").
                orElse(null);

        //then
        assertThat(findByEmailIdUser).isNull();
    }

    @Test
    @DisplayName("전화번호 중복 체크 되는지 테스트")
    void joinPhoneNoDupleTest() throws Exception {
        given(userService.isPhoneNoDuplicate("01012345678")).willReturn(true);
        //given
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("userName", "fish");
        query_param.add("password", "1234WnRnal@");
        query_param.add("verifyPassword", "1234WnRnal@");
        query_param.add("nickName", "takodachi");
        query_param.add("email", "takos@naver.com");
        query_param.add("phoneNo", "01012345678");
        query_param.add("gender", "male");

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/join")
                        .params(query_param)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        User rejectUser = userRepository.findByNickName("takodachi").
                orElse(null);

        //then
        assertThat(rejectUser).isNull();
    }
}