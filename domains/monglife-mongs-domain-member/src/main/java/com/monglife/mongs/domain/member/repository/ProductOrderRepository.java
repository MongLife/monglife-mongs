package com.monglife.mongs.domain.member.repository;

import com.monglife.mongs.domain.member.entity.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Long> {
}
