package com.mongs.play.domain.battle.repository;

import com.mongs.play.domain.battle.entity.BattlePlayer;
import com.mongs.play.domain.battle.entity.BattleRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BattlePlayerRepository extends JpaRepository<BattlePlayer, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT bp FROM BattlePlayer bp WHERE bp.playerId = :playerId")
    Optional<BattlePlayer> findByPlayerIdWithLock(@Param("playerId") String playerId);

    @Query("SELECT bp FROM BattlePlayer bp WHERE bp.playerId = :playerId")
    Optional<BattlePlayer> findByPlayerId(@Param("playerId") String playerId);
}