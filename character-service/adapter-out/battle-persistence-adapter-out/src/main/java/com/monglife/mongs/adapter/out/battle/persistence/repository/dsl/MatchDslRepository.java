package com.monglife.mongs.adapter.out.battle.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchEntity;

import java.util.Optional;

public interface MatchDslRepository {

    Optional<MatchEntity> findByMatchIdWithLock(Long matchId);
}
