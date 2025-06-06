package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.member.persistence.entity.OrderEntity;

import java.util.Optional;

public interface OrderDslRepository {

    Optional<OrderEntity> findByOrderIdWithLock(Long orderId);
}
