package com.monglife.mongs.domain.task.repository;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LockTaskRepository extends JpaRepository<TaskEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TaskEntity> findByTaskId(Long taskId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<TaskEntity> findByAppPackageName(String appPackageName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<TaskEntity> findByAppPackageNameAndTaskOwnerId(String appPackageName, String taskOwnerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<TaskEntity> findByAppPackageNameAndTaskOwnerIdAndComnCode(String appPackageName, String taskOwnerId, String taskCode);
}
