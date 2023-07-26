package com.geulkkoli.domain.post.service;

public enum SearchType {
    TITLE("제목"),
    NICKNAME("닉네임"),
    BODY("내용"),
    HASH_TAG("해시태그"),
    ALL("전체");

    private String type;

    SearchType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
