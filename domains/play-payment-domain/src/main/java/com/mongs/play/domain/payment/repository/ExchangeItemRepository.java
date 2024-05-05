package com.mongs.play.domain.payment.repository;

import com.mongs.play.domain.payment.entity.ExchangeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeItemRepository extends JpaRepository<ExchangeItem, String> {
}
