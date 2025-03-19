package com.monglife.mongs.domain.mong.repositoryCustom;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.mongs.domain.mong.entity.QMongEntity.mongEntity;
import static com.monglife.mongs.domain.mong.entity.QMongMetaEntity.mongMetaEntity;
import static com.monglife.mongs.domain.mong.entity.QMongStateEntity.mongStateEntity;
import static com.monglife.mongs.domain.mong.entity.QMongStatusEntity.mongStatusEntity;

@Repository
public class LockMongDslRepositoryImpl implements LockMongDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public LockMongDslRepositoryImpl(@Qualifier("mongJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<MongEntity> findByMongIdAndMetaIsActiveIsTrue(Long mongId) {

        return Optional.ofNullable(jpaQueryFactory.selectFrom(mongEntity)
                .join(mongEntity.meta, mongMetaEntity).fetchJoin()
                .join(mongEntity.state, mongStateEntity).fetchJoin()
                .join(mongEntity.status, mongStatusEntity).fetchJoin()
                .where(mongEntity.mongId.eq(mongId).and(mongEntity.meta.isActive.eq(true)))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
