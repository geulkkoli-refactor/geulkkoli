package com.geulkkoli.domain.hashtag;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.geulkkoli.domain.hashtag.QHashTag.hashTag;
public class HashTagRepositoryImpl implements HashTagRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public HashTagRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Long> hashIdsByHashTagNames(List<String> hashTagNames) {
        return queryFactory.select(hashTag.hashTagId)
                .from(hashTag)
                .where(hashTag.hashTagName.in(hashTagNames))
                .fetch();
    }

}
