package com.geulkkoli.application;

import lombok.*;

@Setter
@Getter
//@AllArgsConstructor
public class EmailDto {

    // 수신할 회원 이메일, 제목, 내용
    private String to;
    private String subject;
    private String text;

}
