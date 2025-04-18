package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMapEntity;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.monglife.module.common.jpa.entity.QComnCodeEntity.comnCodeEntity;
import static com.monglife.mongs.adapter.out.member.persistence.entity.QCollectionMapEntity.collectionMapEntity;

@Repository
public class CollectionMapDslRepositoryImpl implements CollectionMapDslRepository{

    private static final String mapGroupCode = "MP";

    private final JPAQueryFactory jpaQueryFactory;

    public CollectionMapDslRepositoryImpl(@Qualifier("memberJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CollectionMapEntity> findByAccountId(Long accountId) {

        List<Tuple> tuples = jpaQueryFactory.select(collectionMapEntity, comnCodeEntity)
                .from(collectionMapEntity)
                .rightJoin(collectionMapEntity.mapType, comnCodeEntity)
                .on(collectionMapEntity.mapType.eq(comnCodeEntity), collectionMapEntity.accountId.eq(accountId), comnCodeEntity.group.code.eq(mapGroupCode))
                .orderBy(collectionMapEntity.mapType.code.asc())
                .fetch();

        List<CollectionMapEntity> collectionMapEntities = new ArrayList<>();

        for (Tuple tuple : tuples) {
            ComnCodeEntity comnCode = tuple.get(comnCodeEntity);
            CollectionMapEntity collectionMap = tuple.get(collectionMapEntity);

            if (comnCode != null) {
                if (collectionMap == null) {
                    collectionMap = CollectionMapEntity.builder()
                            .accountId(accountId)
                            .mapType(comnCode)
                            .build();
                    // 보유 여부 제외
                    collectionMap.exclude();
                }

                collectionMapEntities.add(collectionMap);
            }
        }

        return collectionMapEntities;
    }
}
