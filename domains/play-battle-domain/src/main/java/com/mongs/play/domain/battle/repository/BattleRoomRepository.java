package com.mongs.play.domain.battle.repository;

import com.mongs.play.domain.battle.entity.BattleRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BattleRoomRepository extends JpaRepository<BattleRoom, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT br FROM BattleRoom br WHERE br.roomId = :roomId AND br.isActive = true")
    Optional<BattleRoom> findByRoomIdWithLock(@Param("roomId") String roomId);

    @Query("SELECT br FROM BattleRoom br WHERE br.roomId = :roomId AND br.isActive = true")
    Optional<BattleRoom> findByRoomId(@Param("roomId") String roomId);
}