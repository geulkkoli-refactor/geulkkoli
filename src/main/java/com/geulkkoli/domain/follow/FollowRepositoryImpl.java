package com.geulkkoli.domain.follow;

import com.geulkkoli.application.follow.FollowInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.geulkkoli.domain.follow.QFollow.follow;

@Repository
public class FollowRepositoryImpl implements FollowRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FollowRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<FollowInfo> findFollowersByFolloweeUserId(Long followeeId, Long lastFollowId, int pageSize) {
        return queryFactory.select(Projections.constructor(FollowInfo.class,
                        follow.id, follow.follower.userId, follow.follower.nickName, follow.createdAt))
                .from(follow)
                .where(eqLastFollowId(lastFollowId), follow.followee.userId.eq(followeeId))
                .limit(pageSize)
                .orderBy(follow.id.desc())
                .fetch();
    }

    @Override
    public List<Long> findFollowedEachOther(List<Long> followerIds, Long followerId, int pageSize) {
        return queryFactory.select(follow.followee.userId)
                .from(follow)
                .where(follow.followee.userId.in(followerIds), follow.follower.userId.eq(followerId))
                .limit(pageSize)
                .fetch();
    }


    @Override
    public List<FollowInfo> findFolloweesByFollowerUserId(Long followerId, Long lastFollowId, int pageSize) {
        return queryFactory.select(Projections.constructor(FollowInfo.class,
                        follow.id, follow.followee.userId, follow.followee.nickName, follow.createdAt))
                .from(follow)
                .where(eqLastFollowId(lastFollowId), follow.follower.userId.eq(followerId)) // lt : less than , <- and와 같다, id가 lastId보다 작고 followerId가 followerId와 같은 것
                .limit(pageSize)
                .orderBy(follow.id.desc())
                .fetch();
    }

    private BooleanExpression eqLastFollowId(Long lastFollowId) {
        if (lastFollowId == null) {
            return null;
        }
        return follow.id.lt(lastFollowId); //lt : less than , <- and와 같다, id가 lastId보다 작은 것
    }
}
