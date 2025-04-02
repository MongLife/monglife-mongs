package com.monglife.mongs.domain.member.repository;

import com.monglife.mongs.domain.member.entity.ProductOrderEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface LockProductOrderRepository extends JpaRepository<ProductOrderEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductOrderEntity> findByProductOrderId(Long productOrderId);
}
