package com.geulkkoli.application;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {  //자세한 작동 방식은 application.yml에서 확인 가능

    private final JavaMailSender javaMailSender;

    public void sendEmail(EmailDto form) {
        SimpleMailMessage message = new SimpleMailMessage(); // 파일 없이 텍스트만 전송할 때 사용
        message.setTo(form.getTo());
        message.setSubject(form.getSubject());
        message.setText(form.getText());
        javaMailSender.send(message);
    }

    // 인증 번호 보내는 양식 (회원가입 시 이메일 확인)
    public void sendAuthenticationNumberEmail(String email, String authenticationNumber) {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("[바라바라] 회원 가입 인증 번호 안내");
        emailDto.setText("인증 번호: " + authenticationNumber + "\n" + "인증 번호를 입력 후 회원 가입을 완료해주세요.");

        sendEmail(emailDto);
    }

    // 임시 비밀번호 보내는 양식 (비밀번호 찾기)
    public void sendTempPasswordEmail(String email, String tempPassword) {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("[바라바라] 임시 비밀번호 안내");
        emailDto.setText("임시 비밀번호: " + tempPassword + "\n" + "로그인 후 비밀번호를 변경해주세요.");

        sendEmail(emailDto);
    }

}