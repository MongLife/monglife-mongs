package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongEntity.mongEntity;

@Repository
public class MongDslRepositoryImpl implements MongDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MongDslRepositoryImpl(@Qualifier("mongJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<MongEntity> findByMongIdWithLock(Long mongId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(mongEntity)
                .where(mongEntity.mongId.eq(mongId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
