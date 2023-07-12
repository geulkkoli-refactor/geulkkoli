package com.geulkkoli.web.social.util;

import com.geulkkoli.web.social.SocialSignUpDto;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component

public class SocialSignUpValueEncryptoDecryptor {
    private final String key = "1234567890123456";


    public String encryptValue(String value) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            // Handle encryption error
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decryptValue(String encryptedValue) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Handle decryption error
            throw new RuntimeException("Decryption failed", e);
        }
    }

    /**
     * SocialSignUpDto socialSignUpDto = SocialSignUpDto.builder()
     *                 .email(socialSignUpValueEncryptoDecryptor.encryptValue(authUser.getUsername()))
     *                 .nickName(authUser.getNickName())
     *                 .phoneNo(authUser.getPhoneNo())
     *                 .verifyPassword(socialSignUpValueEncryptoDecryptor.encryptValue(authUser.getPassword()))
     *                 .gender(authUser.getGender())
     *                 .userName(socialSignUpValueEncryptoDecryptor.encryptValue(authUser.getName()))
     *                 .password(socialSignUpValueEncryptoDecryptor.encryptValue(authUser.getPassword()))
     *                 .authorizationServerId(socialSignUpValueEncryptoDecryptor.encryptValue(authUser.getUserId()))
     *                 .clientregistrationName(socialSignUpValueEncryptoDecryptor.encryptValue(authUser.getSocialType().getValue()))
     *                 .build();
     *
     */

    public SocialSignUpDto decryptValue(SocialSignUpDto signUpDtoUpDto) {
        return SocialSignUpDto.builder()
                .email(decryptValue(signUpDtoUpDto.getEmail()))
                .nickName(signUpDtoUpDto.getNickName())
                .phoneNo(signUpDtoUpDto.getPhoneNo())
                .password(signUpDtoUpDto.getPassword())
                .verifyPassword(signUpDtoUpDto.getPassword())
                .authorizationServerId(decryptValue(signUpDtoUpDto.getAuthorizationServerId()))
                .userName(decryptValue(signUpDtoUpDto.getUserName()))
                .gender(signUpDtoUpDto.getGender())
                .clientregistrationName(decryptValue(signUpDtoUpDto.getClientregistrationName()))
                .build();

    }
}
