package com.geulkkoli.domain.hashtag.util;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagType;

import javax.persistence.AttributeConverter;

public class HashTagTypeConverter implements AttributeConverter<HashTagType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(HashTagType attribute) {
        return attribute.getTypeCode();
    }

    @Override
    public HashTagType convertToEntityAttribute(Integer dbData) {
        return HashTagType.hashTageType(dbData);
    }
}
