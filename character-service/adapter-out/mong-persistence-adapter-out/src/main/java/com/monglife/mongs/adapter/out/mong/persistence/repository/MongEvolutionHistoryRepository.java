package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEvolutionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MongEvolutionHistoryRepository extends JpaRepository<MongEvolutionHistoryEntity, Long> {

    boolean existsByAccountIdAndMongCode(Long accountId, String mongCode);

    List<MongEvolutionHistoryEntity> findByAccountId(Long accountId);
}
