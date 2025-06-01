package com.monglife.mongs.adapter.out.battle.persistence.repository;

import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    Optional<MatchEntity> findByMatchId(Long matchId);
}
