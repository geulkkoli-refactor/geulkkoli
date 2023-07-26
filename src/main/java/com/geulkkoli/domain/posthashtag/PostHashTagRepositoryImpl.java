package com.geulkkoli.domain.posthashtag;

import com.geulkkoli.domain.post.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.geulkkoli.domain.posthashtag.QPostHashTag.postHashTag;

public class PostHashTagRepositoryImpl implements PostHashTagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostHashTagRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public List<Post> findAllByHashTagNames(List<String> hashTagNames) {
        return queryFactory.select(postHashTag.post)
                .from(postHashTag)
                .where(postHashTag.hashTag.hashTagName.in(hashTagNames))
                .fetch();
    }
}
