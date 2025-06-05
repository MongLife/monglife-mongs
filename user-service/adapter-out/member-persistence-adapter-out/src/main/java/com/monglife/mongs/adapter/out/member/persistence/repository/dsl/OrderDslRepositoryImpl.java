package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.member.persistence.entity.OrderEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.mongs.adapter.out.member.persistence.entity.QOrderEntity.orderEntity;

@Repository
public class OrderDslRepositoryImpl implements OrderDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public OrderDslRepositoryImpl(@Qualifier("memberJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<OrderEntity> findByOrderIdWithLock(Long orderId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(orderEntity)
                .where(orderEntity.orderId.eq(orderId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
