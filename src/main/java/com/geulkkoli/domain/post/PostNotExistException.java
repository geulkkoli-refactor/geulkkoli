package com.geulkkoli.domain.post;

public class PostNotExistException extends RuntimeException {
    public PostNotExistException(String message) {
        super(message);
    }
}
