package com.geulkkoli.application.user.util;

import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.security.util.RoleNameAttributeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleNameAttributeConverterTest {

    @Test
    @DisplayName("데이터베이스 컬럼 변환 테스트")
    void convertToDatabaseColumn() {
        RoleNameAttributeConverter converter = new RoleNameAttributeConverter();
        assertEquals(2, converter.convertToDatabaseColumn(Role.USER));
        assertEquals(1, converter.convertToDatabaseColumn(Role.ADMIN));
    }

    @Test
    @DisplayName("데이터베이스 컬럼 엔티티 필드 변환 테스트")
    void convertToEntityAttribute() {
        RoleNameAttributeConverter converter = new RoleNameAttributeConverter();
        assertEquals(Role.USER, converter.convertToEntityAttribute(2));
        assertEquals(Role.ADMIN, converter.convertToEntityAttribute(1));
    }
}