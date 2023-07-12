package com.geulkkoli.domain.hashtag;

public enum HashTagSign {
    GENERAL("#");

    private final String sign;

    HashTagSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
