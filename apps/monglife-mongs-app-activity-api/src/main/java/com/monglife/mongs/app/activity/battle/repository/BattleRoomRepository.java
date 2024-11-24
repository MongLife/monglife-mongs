package com.monglife.mongs.app.activity.battle.repository;

import com.monglife.mongs.app.activity.battle.domain.BattleRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BattleRoomRepository extends JpaRepository<BattleRoomEntity, Long> {

    Optional<BattleRoomEntity> findByRoomIdAndIsActiveTrue(Long roomId);

    Optional<BattleRoomEntity> findByRoomIdAndIsActiveFalse(Long roomId);

    Optional<BattleRoomEntity> findByRoomIdAndIsActiveFalseAndRound(Long roomId, Integer round);
}
