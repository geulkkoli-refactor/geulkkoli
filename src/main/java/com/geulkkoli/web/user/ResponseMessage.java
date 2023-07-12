package com.geulkkoli.web.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseMessage {
    NULL_OR_BLANK_EMAIL("nullOrBlank","이메일을 입력해주세요."),
    EMAIL_DUPLICATION("emailDuplicated","이미 가입된 이메일입니다."),
    SEND_AUTHENTICATION_NUMBER_SUCCESS("sendAuthenticationNumberEmail","인증 번호가 전송되었습니다.");

    private String code;
    private String message;

    ResponseMessage(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Boolean isSuccess() {
    	return this.code.equals("sendAuthenticationNumberEmail");
    }

    public Boolean isDuplicated() {
    	return this.code.equals("emailDuplicated");
    }

    public Boolean isNullOrBlank() {
    	return this.code.equals("nullOrBlank");
    }
}
