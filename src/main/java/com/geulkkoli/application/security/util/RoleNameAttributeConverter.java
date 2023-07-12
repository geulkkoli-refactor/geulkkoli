package com.geulkkoli.application.security.util;

import com.geulkkoli.application.security.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.util.stream.Stream;

/**
 * Created by hoogokok on 2023/4/21
 * Permission Entity의 Role 필드를 DB에 저장할 때 Enum 타입이 아닌 Integer 타입으로 저장하기 위한 Converter
 */
@Convert
public class RoleNameAttributeConverter implements AttributeConverter<Role, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Role attribute) {
        if (Role.USER.equals(attribute)) {
            return 2;
        } else if (Role.ADMIN.equals(attribute)) {
            return 1;
        } else {
            return 3;
        }

    }

    @Override
    public Role convertToEntityAttribute(Integer dbData) {

        return Stream.of(Role.values())
                .filter(role -> role.getRoleCode() == dbData)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
