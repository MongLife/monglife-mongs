package com.monglife.mongs.app.battle.repository;

import com.monglife.mongs.app.battle.domain.BattleRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRoomRepository extends JpaRepository<BattleRoomEntity, Long> {
}
