package com.monglife.mongs.domain.task.repository;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTaskId(Long taskId);

    Optional<TaskEntity> findByAppPackageNameAndTaskOwnerIdAndComnCode(String appPackageName, String taskOwnerId, String taskCode);

    Optional<TaskEntity> findByAppPackageNameAndTaskOwnerIdAndComnCodeAndTaskStatusCode(String appPackageName, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode);

    Set<TaskEntity> findByTaskIdNotInAndTaskStatusCode(Set<Long> taskIds, TaskStatusCode taskStatusCode);
}
