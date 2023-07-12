package com.geulkkoli.web.user.dto;

public class EmailCheckResponseMessage {
    private String message;

    public EmailCheckResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
