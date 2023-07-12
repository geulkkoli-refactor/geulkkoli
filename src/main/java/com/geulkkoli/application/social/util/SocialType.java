package com.geulkkoli.application.social.util;

import java.util.Arrays;

public enum SocialType {
    KAKAO("kakao"), NAVER("naver"), GOOGLE("google");

    private String value;

    SocialType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static SocialType findByProviderName(String value) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new SocialTypeException("해당하는 소셜 타입이 없습니다."));
    }

    public boolean is(String clientregistrationName) {
        return this.value.equals(clientregistrationName);
    }
}
