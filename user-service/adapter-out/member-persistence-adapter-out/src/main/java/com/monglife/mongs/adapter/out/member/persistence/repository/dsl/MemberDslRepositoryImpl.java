package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.member.persistence.entity.MemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.mongs.adapter.out.member.persistence.entity.QMemberEntity.memberEntity;

@Repository
public class MemberDslRepositoryImpl implements MemberDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberDslRepositoryImpl(@Qualifier("memberJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<MemberEntity> findByAccountIdWithLock(Long accountId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(memberEntity)
                .where(memberEntity.accountId.eq(accountId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
