package com.monglife.mongs.adapter.out.member.persistence.repository;

import com.monglife.mongs.adapter.out.member.persistence.entity.ExchangeStarPointProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeStarPointProductRepository extends JpaRepository<ExchangeStarPointProductEntity, String> {

    Optional<ExchangeStarPointProductEntity> findByProductId(String productId);
}
