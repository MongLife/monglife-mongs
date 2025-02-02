package com.monglife.mongs.domain.match.repository;

import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRoomRepository extends JpaRepository<MatchRoomEntity, Long> {

    Optional<MatchRoomEntity> findByRoomIdAndIsActiveTrue(Long roomId);

    Optional<MatchRoomEntity> findByRoomIdAndIsActiveTrueAndRound(Long roomId, Integer round);

    Optional<MatchRoomEntity> findByRoomIdAndIsActiveFalse(Long roomId);

    Optional<MatchRoomEntity> findByRoomIdAndIsActiveFalseAndRound(Long roomId, Integer round);
}
