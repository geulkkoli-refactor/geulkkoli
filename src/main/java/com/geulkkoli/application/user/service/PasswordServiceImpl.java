package com.geulkkoli.application.user.service;

import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserNotExistException;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.user.dto.edit.PasswordEditFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";
    private static final String ALL_CHAR = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;


    @Override
    public boolean isPasswordVerification(User user, PasswordEditFormDto passwordEditFormDto) {
        return passwordEncoder.matches(passwordEditFormDto.getOldPassword(), user.getPassword());
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
       User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException("해당 유저가 존재하지 않습니다."));
       user.updatePassword(passwordEncoder.encode(newPassword));
       userRepository.save(user);
    }

    @Override
    public int setLength(int startLength, int endLength) {
        int length = 0;
        while (length < startLength)
            length = random.nextInt(endLength + 1); // nextInt 메소드 범위: 0 ~ n-1

        return length;
    }

    @Override
    public String createTempPassword(int length) {
        StringBuilder tempPassword = new StringBuilder(length);

        // 비밀번호에 영문/숫자/특문 각 하나씩 추가
        tempPassword.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())))
                .append(NUMBER.charAt(random.nextInt(NUMBER.length())))
                .append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        // 이제 나머지 비밀번호 자릿수 채워줄 랜덤값
        for (int i = 3; i < length; i++)
            tempPassword.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));

        return tempPassword.toString();
    }

    @Override
    public String authenticationNumber(int length) {
        StringBuilder authenticationNumber = new StringBuilder(length);

        for (int i = 1; i <= length; i++)
            authenticationNumber.append(NUMBER.charAt(random.nextInt(NUMBER.length())));

        return authenticationNumber.toString();
    }

}
