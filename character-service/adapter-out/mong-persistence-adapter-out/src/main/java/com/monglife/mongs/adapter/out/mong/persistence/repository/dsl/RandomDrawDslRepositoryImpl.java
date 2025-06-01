package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.RandomDrawEntity;
import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.monglife.mongs.adapter.out.mong.persistence.entity.QRandomDrawEntity.randomDrawEntity;
import static com.monglife.mongs.adapter.out.mong.persistence.entity.QRandomDrawHistoryEntity.randomDrawHistoryEntity;

@Repository
public class RandomDrawDslRepositoryImpl implements RandomDrawDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public RandomDrawDslRepositoryImpl(@Qualifier("mongJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<RandomDrawEntity> findNotDrawByAccountId(Long accountId) {

        BooleanBuilder randomDrawMap = new BooleanBuilder();
        randomDrawMap.and(randomDrawEntity.inventoryTypeCode.eq(InventoryTypeCode.MAP));
        randomDrawMap.and(randomDrawEntity.comn.code.notIn(
                JPAExpressions
                        .select(randomDrawHistoryEntity.comn.code)
                        .from(randomDrawHistoryEntity)
                        .where(randomDrawHistoryEntity.accountId.eq(accountId))));
        BooleanExpression randomDrawOther = randomDrawEntity.inventoryTypeCode.ne(InventoryTypeCode.MAP);

        return jpaQueryFactory.selectFrom(randomDrawEntity)
                .where(randomDrawMap.or(randomDrawOther))
                .fetch();
    }
}
