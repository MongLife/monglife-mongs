package com.mongs.play.domain.payment.repository;

import com.mongs.play.domain.payment.entity.ChargeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeItemRepository extends JpaRepository<ChargeItem, String> {
}
