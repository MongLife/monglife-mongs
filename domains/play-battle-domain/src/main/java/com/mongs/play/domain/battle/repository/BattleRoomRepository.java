package com.mongs.play.domain.battle.repository;

import com.mongs.play.domain.battle.entity.BattleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRoomRepository extends JpaRepository<BattleRoom, String> {
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT t FROM TaskEvent t WHERE t.taskStatus = :taskStatus")
//    List<TaskEvent> findByTaskStatusWithLock(@Param("taskStatus") TaskStatus taskStatus);
}