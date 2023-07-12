package com.geulkkoli.domain.follow.service;

public class CanNotFollowException extends RuntimeException {
    public CanNotFollowException(String message) {
        super(message);
    }
}
