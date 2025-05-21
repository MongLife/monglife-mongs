package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongTypeEntity;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.monglife.mongs.adapter.out.mong.persistence.entity.QMongTypeEntity.mongTypeEntity;

@Repository
public class MongTypeDslRepositoryImpl implements MongTypeDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MongTypeDslRepositoryImpl(@Qualifier("mongJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<MongTypeEntity> findByEvolutionScoreAndMongTypeCode(Double evolutionScore, String mongTypeCode) {
        return jpaQueryFactory.selectFrom(mongTypeEntity)
                .where(mongTypeEntity.groupType.eq(
                        JPAExpressions
                                .select(mongTypeEntity.nextGroupType)
                                .from(mongTypeEntity)
                                .where(mongTypeEntity.comn.code.eq(mongTypeCode))))
                .where(mongTypeEntity.evolutionScore.loe(evolutionScore))
                .orderBy(mongTypeEntity.evolutionScore.asc())
                .fetch();
    }
}
