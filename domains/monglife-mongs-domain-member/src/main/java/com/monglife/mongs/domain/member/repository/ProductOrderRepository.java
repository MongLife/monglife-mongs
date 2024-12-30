package com.monglife.mongs.domain.member.repository;

import com.monglife.mongs.domain.member.entity.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Long> {

    Optional<ProductOrderEntity> findByPurchaseToken(String purchaseToken);
}
