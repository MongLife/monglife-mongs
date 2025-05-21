package com.monglife.mongs.adapter.out.battle.persistence.repository;

import com.monglife.mongs.domain.battle.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
