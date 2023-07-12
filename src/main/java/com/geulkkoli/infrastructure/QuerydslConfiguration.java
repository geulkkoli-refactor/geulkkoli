package com.geulkkoli.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * QuerydslConfiguration은 JPAQueryFactory를 Bean으로 등록하기 위한 클래스입니다.
 */
@Configuration
public class QuerydslConfiguration {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean

    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
