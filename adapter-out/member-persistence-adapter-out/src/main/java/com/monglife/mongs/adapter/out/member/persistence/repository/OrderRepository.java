package com.monglife.mongs.adapter.out.member.persistence.repository;

import com.monglife.mongs.adapter.out.member.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByIsConsumedIsTrueAndSocialOrderIdIn(List<String> socialOrderIds);

    Optional<OrderEntity> findBySocialOrderId(String socialOrderId);
}
