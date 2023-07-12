package com.geulkkoli.web.user.dto;

import com.geulkkoli.domain.user.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@ToString
@RequiredArgsConstructor
public class JoinFormDto {

    // NotEmpty가 NotNull를 포함하므로 삭제
    @NotEmpty
    private String userName;

    @NotEmpty
    @Length(min = 8, max = 20)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*\\W])(?=\\S+$).{8,20}")
    private String password;

    @NotEmpty
    private String verifyPassword;

    @NotEmpty
    @Length(min = 2, max = 8)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$")
    private String nickName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Length(min = 10, max = 11)
    @Pattern(regexp = "^[*\\d]*$")
    private String phoneNo;

    @NotEmpty
    private String gender;


    public static JoinFormDto of(String userName, String password, String verifyPassword, String nickName, String email, String phoneNo, String gender){
        return new JoinFormDto(userName,password,verifyPassword,nickName,email,phoneNo,gender);
    }

    private JoinFormDto(String userName, String password, String verifyPassword, String nickName, String email, String phoneNo, String gender) {
        this.userName = userName;
        this.password = password;
        this.verifyPassword = verifyPassword;
        this.nickName = nickName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .nickName(nickName)
                .email(email)
                .phoneNo(phoneNo)
                .gender(gender)
                .build();

    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }
}
