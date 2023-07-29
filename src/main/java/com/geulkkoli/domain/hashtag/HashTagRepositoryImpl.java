package com.geulkkoli.domain.hashtag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.geulkkoli.domain.hashtag.QHashTag.hashTag;

@Repository
public class HashTagRepositoryImpl implements HashTagRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public HashTagRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<HashTag> findAllHashTagByHashTagNames(List<String> hashTagNames) {
        return queryFactory.select(hashTag)
                .from(hashTag)
                .where(hashTag.hashTagName.in(hashTagNames))
                .fetch();
    }}
