package com.geulkkoli.domain.user.service;

import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.security.RoleEntity;
import com.geulkkoli.application.security.RoleRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserNotExistException;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.home.dto.JoinDTO;
import com.geulkkoli.web.account.dto.edit.UserInfoEditFormDto;
import com.geulkkoli.web.social.SocialSignUpDto;
import com.geulkkoli.web.account.dto.edit.PasswordEditFormDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isPasswordVerification(User user, PasswordEditFormDto passwordEditFormDto) {
        return passwordEncoder.matches(passwordEditFormDto.getOldPassword(), user.getPassword());
    }

    public boolean isPasswordVerification(String currentPassword, String oldPassword) {
        return passwordEncoder.matches(oldPassword, currentPassword);
    }

    public User updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException("해당 유저가 존재하지 않습니다."));
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return user;
    }

    public boolean updatePassword(User user, PasswordEditFormDto passwordEditFormDto) {
        if (passwordEditFormDto.isMatched()){
            user.updatePassword(passwordEncoder.encode(passwordEditFormDto.getNewPassword()));
            userRepository.save(user);
            return true;
        }

        return false;
    }


    public void edit(Long id, UserInfoEditFormDto userInfoEditDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException("해당 유저가 존재하지 않습니다."));
        user.updateUserName(userInfoEditDto.getUserName());
        user.updateNickName(userInfoEditDto.getNickName());
        user.updatePhoneNo(userInfoEditDto.getPhoneNo());
        user.updateGender(userInfoEditDto.getGender());
        userRepository.save(user);
    }


    public User signUp(JoinDTO form) {
        User user = form.toEntity(passwordEncoder);
        RoleEntity roleEntity = user.addRole(Role.USER);
        roleRepository.save(roleEntity);
        return userRepository.save(user);
    }

    /*
     * 관리자 실험을 위한 임시 관리자 계정 추가용 메서드*/
    public void signUpAdmin(JoinDTO form) {
        User user = form.toEntity(passwordEncoder);
        RoleEntity roleEntity = user.addRole(Role.ADMIN);
        roleRepository.save(roleEntity);
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.deleteById(user.getUserId());
    }


    public User signUp(SocialSignUpDto signUpDto) {
        User user = signUpDto.toEntity(passwordEncoder);
        RoleEntity role = user.addRole(Role.USER);
        roleRepository.save(role);
        userRepository.save(user);
        return user;
    }
}
