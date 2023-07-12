package com.geulkkoli.application.security;

public class LockExpiredTimeException extends RuntimeException{
    public LockExpiredTimeException(String message) {
        super(message);
    }
}
