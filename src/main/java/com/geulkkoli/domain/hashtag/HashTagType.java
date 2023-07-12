package com.geulkkoli.domain.hashtag;

import java.util.Arrays;

public enum HashTagType {
    GENERAL("일반", 1),
    CATEGORY("분류", 2),
    STATUS("상태", 3),
    MANAGEMENT("관리", 4);

    private final String typeName;
    private final int typeCode;

    HashTagType(String typeName, int typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }

    public static HashTagType getHashTagType(String type) {
        return Arrays.stream(HashTagType.values())
                .filter(hashTagType -> hashTagType.typeName.equals(type))
                .findFirst()
                .orElse(null);
    }

    public static HashTagType hashTageType(Integer dbData) {
        return Arrays.stream(HashTagType.values())
                .filter(hashTagType -> hashTagType.typeCode == dbData)
                .findFirst()
                .orElse(null);
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
