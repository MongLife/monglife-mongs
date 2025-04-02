package com.mongs.play.domain.task.repository;

import com.mongs.play.domain.task.entity.TaskEvent;
import com.mongs.play.domain.task.enums.TaskCode;
import com.mongs.play.domain.task.enums.TaskStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskEventRepository extends JpaRepository<TaskEvent, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TaskEvent t WHERE t.taskStatus = :taskStatus")
    List<TaskEvent> findByTaskStatusWithLock(@Param("taskStatus") TaskStatus taskStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TaskEvent t WHERE t.mongId = :mongId")
    List<TaskEvent> findByMongIdWithLock(@Param("mongId") Long mongId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TaskEvent t WHERE t.mongId = :mongId AND t.taskCode = :taskCode AND t.taskStatus = :taskStatus")
    Optional<TaskEvent> findByMongIdAndTaskCodeAndTaskStatusWithLock(@Param("mongId") Long mongId, @Param("taskCode") TaskCode taskCode, @Param("taskStatus") TaskStatus taskStatus);

    @Query("SELECT t FROM TaskEvent t WHERE t.mongId = :mongId AND t.taskCode = :taskCode AND t.taskStatus = :taskStatus")
    Optional<TaskEvent> findByMongIdAndTaskCodeAndTaskStatus(@Param("mongId") Long mongId, TaskCode taskCode, TaskStatus taskStatus);
}