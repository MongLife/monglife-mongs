package com.monglife.mongs.adapter.out.battle.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.mongs.adapter.out.battle.persistence.entity.QMatchEntity.matchEntity;

@Repository
public class MatchDslRepositoryImpl implements MatchDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MatchDslRepositoryImpl(@Qualifier("battleJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<MatchEntity> findByMatchIdWithLock(Long matchId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(matchEntity)
                .where(matchEntity.matchId.eq(matchId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
