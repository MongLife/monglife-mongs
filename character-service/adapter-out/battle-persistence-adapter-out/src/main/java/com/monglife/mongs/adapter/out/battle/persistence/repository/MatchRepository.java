package com.monglife.mongs.adapter.out.battle.persistence.repository;

import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
}
