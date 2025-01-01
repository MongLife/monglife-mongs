package com.monglife.mongs.domain.member.repository;

import com.monglife.mongs.domain.member.entity.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Long> {

    Optional<ProductOrderEntity> findByProductOrderId(Long productOrderId);

    Optional<ProductOrderEntity> findByOrderId(String orderId);

    List<ProductOrderEntity> findAllByOrderIdIn(List<String> orderIds);
}
