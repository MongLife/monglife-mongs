package com.monglife.mongs.domain.task.repository;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface LockTaskRepository extends JpaRepository<TaskEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TaskEntity> findByTaskId(Long taskId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<TaskEntity> findByAppCode(String appCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<TaskEntity> findByAppCodeAndTaskOwnerId(String appCode, String taskOwnerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TaskEntity> findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(String appCode, String taskOwnerId, String taskCode);
}
