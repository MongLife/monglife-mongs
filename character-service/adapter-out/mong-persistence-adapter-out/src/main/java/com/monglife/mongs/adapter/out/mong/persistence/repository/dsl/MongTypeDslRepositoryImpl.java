package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongTypeEntity;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongGroupTypeEntity.mongGroupTypeEntity;
import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongTypeEntity.mongTypeEntity;

@Repository
public class MongTypeDslRepositoryImpl implements MongTypeDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MongTypeDslRepositoryImpl(@Qualifier("mongJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<MongTypeEntity> findMongCode(String mongCode) {
        return jpaQueryFactory.selectFrom(mongTypeEntity)
                .join(mongTypeEntity.comn).fetchJoin()
                .where(mongTypeEntity.groupType.in(
                        JPAExpressions
                                .select(mongGroupTypeEntity.nextGroupType)
                                .from(mongGroupTypeEntity)
                                .join(mongTypeEntity).on(mongGroupTypeEntity.groupType.eq(mongTypeEntity.groupType))
                                .where(mongTypeEntity.comn.code.eq(mongCode))))
                .orderBy(mongTypeEntity.evolutionScore.asc())
                .fetch();
    }
}
