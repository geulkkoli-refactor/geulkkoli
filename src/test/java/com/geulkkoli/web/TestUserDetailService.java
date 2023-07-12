package com.geulkkoli.web;

import com.geulkkoli.application.security.AccountStatus;
import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.application.user.UserModelDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModelDto testModel = UserModelDto.builder()
                .authorizaionServerId("1")
                .nickName("바나나")
                .email("email")
                .password("123qwe!@#")
                .phoneNo("0101223671")
                .userName("김")
                .gender("Male")
                .build();
        List<GrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return CustomAuthenticationPrinciple.from(testModel,simpleGrantedAuthorities, AccountStatus.ACTIVE);
    }
}
