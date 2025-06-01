package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMongEntity;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.monglife.module.common.jpa.entity.QComnCodeEntity.comnCodeEntity;
import static com.monglife.mongs.adapter.out.member.persistence.entity.QCollectionMongEntity.collectionMongEntity;

@Repository
public class CollectionMongDslRepositoryImpl implements CollectionMongDslRepository {

    private static final String MONG_GROUP_CODE = "CH";

    private final JPAQueryFactory jpaQueryFactory;

    public CollectionMongDslRepositoryImpl(@Qualifier("memberJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CollectionMongEntity> findByAccountId(Long accountId) {

        List<Tuple> tuples = jpaQueryFactory.select(collectionMongEntity, comnCodeEntity)
                .from(collectionMongEntity)
                .rightJoin(collectionMongEntity.comn, comnCodeEntity)
                .on(collectionMongEntity.comn.eq(comnCodeEntity), collectionMongEntity.accountId.eq(accountId))
                .where(comnCodeEntity.group.code.eq(MONG_GROUP_CODE))
                .orderBy(collectionMongEntity.comn.code.asc())
                .fetch();

        List<CollectionMongEntity> collectionMongEntities = new ArrayList<>();

        for (Tuple tuple : tuples) {
            ComnCodeEntity comnCode = tuple.get(comnCodeEntity);
            CollectionMongEntity collectionMong = tuple.get(collectionMongEntity);

            if (comnCode != null) {
                if (collectionMong == null) {
                    collectionMong = CollectionMongEntity.builder()
                            .accountId(accountId)
                            .comn(comnCode)
                            .build();
                    // 보유 여부 제외
                    collectionMong.exclude();
                }

                collectionMongEntities.add(collectionMong);
            }
        }

        return collectionMongEntities;
    }
}
