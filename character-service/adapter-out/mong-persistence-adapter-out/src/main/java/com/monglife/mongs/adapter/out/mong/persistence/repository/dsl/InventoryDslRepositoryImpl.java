package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.InventoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.mongs.adapter.out.mong.persistence.entity.QInventoryEntity.inventoryEntity;

@Repository
public class InventoryDslRepositoryImpl implements InventoryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public InventoryDslRepositoryImpl(@Qualifier("mongJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<InventoryEntity> findByIdWithLock(Long inventoryId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(inventoryEntity)
                .where(inventoryEntity.inventoryId.eq(inventoryId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
