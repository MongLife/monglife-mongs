package com.monglife.mongs.domain.task.repository;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByAppCodeAndTaskOwnerId(String appCode, String taskOwnerId);

    Optional<TaskEntity> findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(String appCode, String taskOwnerId, String taskCode);
}
