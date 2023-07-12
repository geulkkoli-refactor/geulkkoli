package com.geulkkoli.domain.user;

public class UserNotExistException extends RuntimeException {

    public UserNotExistException(String message) {
        super(message);
    }
}
