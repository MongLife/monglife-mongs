package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongEntity.mongEntity;
import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongMetaEntity.mongMetaEntity;
import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongStateEntity.mongStateEntity;
import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongStatusEntity.mongStatusEntity;

@Repository
public class MongDslRepositoryImpl implements MongDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MongDslRepositoryImpl(@Qualifier("mongJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<MongEntity> findByMongIdAndAccountIdAndMetaIsActiveIsTrue(Long mongId, Long accountId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(mongEntity)
                .join(mongEntity.meta, mongMetaEntity).fetchJoin()
                .join(mongEntity.state, mongStateEntity).fetchJoin()
                .join(mongEntity.status, mongStatusEntity).fetchJoin()
                .where(mongEntity.mongId.eq(mongId)
                .and(mongEntity.accountId.eq(accountId))
                .and(mongEntity.meta.isActive.eq(true)))
                .fetchOne());
    }

    @Override
    public Optional<MongEntity> findByMongIdAndMetaIsActiveIsTrue(Long mongId) {

        return Optional.ofNullable(jpaQueryFactory.selectFrom(mongEntity)
                .join(mongEntity.meta, mongMetaEntity).fetchJoin()
                .join(mongEntity.state, mongStateEntity).fetchJoin()
                .join(mongEntity.status, mongStatusEntity).fetchJoin()
                .where(mongEntity.mongId.eq(mongId)
                .and(mongEntity.meta.isActive.eq(true)))
                .fetchOne());
    }

    @Override
    public List<MongEntity> findByAccountIdAndMetaIsActiveIsTrue(Long accountId) {
        return jpaQueryFactory.selectFrom(mongEntity)
                .join(mongEntity.meta, mongMetaEntity).fetchJoin()
                .join(mongEntity.state, mongStateEntity).fetchJoin()
                .join(mongEntity.status, mongStatusEntity).fetchJoin()
                .where(mongEntity.accountId.eq(accountId))
                .fetch();
    }
}
