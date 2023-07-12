package com.geulkkoli.domain.follow.service;

public class FollowNotFoundException extends RuntimeException {
    public FollowNotFoundException(String message) {
        super(message);
    }
}
