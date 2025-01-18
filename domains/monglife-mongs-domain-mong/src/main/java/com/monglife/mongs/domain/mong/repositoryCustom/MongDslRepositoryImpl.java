package com.monglife.mongs.domain.mong.repositoryCustom;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.monglife.mongs.domain.mong.entity.QMongEntity.mongEntity;
import static com.monglife.mongs.domain.mong.entity.QMongMetaEntity.mongMetaEntity;
import static com.monglife.mongs.domain.mong.entity.QMongStateEntity.mongStateEntity;
import static com.monglife.mongs.domain.mong.entity.QMongStatusEntity.mongStatusEntity;

@Repository
@RequiredArgsConstructor
public class MongDslRepositoryImpl implements MongDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

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
