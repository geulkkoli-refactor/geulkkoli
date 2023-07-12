package com.geulkkoli.domain.post;

public class NotAuthorException extends RuntimeException {
    public NotAuthorException(String message) {
        super(message);
    }
}
