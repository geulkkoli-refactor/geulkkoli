package com.geulkkoli.domain.user.service;

import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserFindService {
    private final UserRepository userRepository;

    public UserFindService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No user found id matches:" + id));
    }
    @Transactional(readOnly = true)
    public boolean isEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isNickNameDuplicate(String nickName) {
        return userRepository.findByNickName(nickName).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isPhoneNoDuplicate(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo).isPresent();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUserNameAndPhoneNo(String userName, String phoneNo) {
        return userRepository.findByUserNameAndPhoneNo(userName, phoneNo);
    }

    public Optional<User> findByEmailAndUserNameAndPhoneNo(String email, String userName, String phoneNo) {
        return userRepository.findByEmailAndUserNameAndPhoneNo(email, userName, phoneNo);
    }

    public User findByNickName(String nickName){
        return userRepository.findByNickName(nickName).orElseThrow(() -> new NoSuchElementException("No user found nickname matches:" + nickName));
    }
}
