package com.geulkkoli.domain.post;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.geulkkoli.domain.hashtag.QHashTag.hashTag;
import static com.geulkkoli.domain.post.QPost.post;
import static com.geulkkoli.domain.posthashtag.QPostHashTag.postHashTag;

/**
 * PostRepositoryImpl은 querydsl를 이용하기  위해 만든 클래스입니다.
 * 굳이 이 방식을 쓰지 않아도 되지만 그렇게 할 시 qurydsl 설정을 알기 힘들 수도 있어 조금 예전 방식을 써보았습니다.
 * 이 방식은 querydsl를 이용해 update, delete를 할 수 있게 해줍니다.
 * querydsl을 통해 복잡한 동적 쿼리를 작성하길 추천합니다.
 * 참고: https://jojoldu.tistory.com/372
 * how to use: https://www.baeldung.com/intro-to-querydsl
 * how to user page: https://www.baeldung.com/rest-api-pagination-in-spring
 * 를 참고 해보아도 좋을 거 같습니다.
 */
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public void update(Long postId, Post updateParam) {
        QPost post = QPost.post;

        queryFactory.update(post)
                .set(post.title, updateParam.getTitle())
                .set(post.postBody, updateParam.getPostBody())
                .where(post.postId.eq(postId))
                .execute();
    }

    @Override
    public List<Post> postsByHashTag(String searchWords) {

        return queryFactory.selectFrom(post)
                .join(postHashTag).on(postHashTag.post.eq(post))
                .join(hashTag).on(postHashTag.hashTag.eq(hashTag))
                .where(hashTag.hashTagName.eq(searchWords))
                .fetch();
    }

    @Override
    public List<Post> allPostsMultiHashTags(List<String> hashTagNames) {

        SubQueryExpression<Post> subquery = multiHashTag(hashTagNames);

        return queryFactory.selectFrom(post)
                .where(post.in(subquery))
                .fetch();
    }

    @Override
    public List<Tuple> allPostsMultiHashTagsWithTuple(String hashTagName, String hashTagName2) {
        return queryFactory
                .select(post.postId, post.title, post.postBody,hashTag.hashTagName)
                .from(postHashTag)
                .join(hashTag).on(postHashTag.hashTag.eq(hashTag))
                .join(post).on(postHashTag.post.eq(post))
                .where(hashTag.hashTagName.eq(hashTagName).or(hashTag.hashTagName.eq(hashTagName2)))
                .fetch();
    }

    @Override
    public List<Post> allPostsTitleAndMultiPosts(String title, List<String> hashTagNames) {
        SubQueryExpression<Post> subquery = multiHashTag(hashTagNames);

        return queryFactory.selectFrom(post)
                .where(post.title.contains(title))
                .where(post.in(subquery))
                .fetch();
    }

    private JPQLQuery<Post> multiHashTag(List<String> hashTagNames) {
        return JPAExpressions.selectDistinct(postHashTag.post)
                .from(postHashTag)
                .join(postHashTag.hashTag, hashTag)
                .where(hashTag.hashTagName.in(hashTagNames))
                .groupBy(postHashTag.post)
                .having(hashTag.countDistinct().goe((long) hashTagNames.size()));
    }

}
