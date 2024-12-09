package com.monglife.mongs.domain.task.repository;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTaskId(Long taskId);

    Optional<TaskEntity> findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(String appCode, String taskOwnerId, String taskCode);

    Optional<TaskEntity> findByAppCodeAndTaskOwnerIdAndTaskCodeComnCodeAndTaskStatusCode(String appCode, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode);
}
