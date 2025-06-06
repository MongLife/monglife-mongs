package com.monglife.mongs.adapter.out.member.persistence.repository;

import com.monglife.mongs.adapter.out.member.persistence.entity.OrderEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.dsl.OrderDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, OrderDslRepository {

    Boolean existsByAccountIdAndSocialOrderId(Long accountId, String socialOrderId);

    Optional<OrderEntity> findBySocialOrderId(String socialOrderId);
}
