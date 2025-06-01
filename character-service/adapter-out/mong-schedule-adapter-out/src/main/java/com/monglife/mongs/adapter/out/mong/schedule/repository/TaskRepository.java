package com.monglife.mongs.adapter.out.mong.schedule.repository;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.repository.dsl.TaskDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskDslRepository {

    Optional<TaskEntity> findByTaskId(Long taskId);

    Optional<TaskEntity> findByAppPackageNameAndMongIdAndSchedulerTypeCode(String appPackageName, Long mongId, String schedulerTypeCode);
}
