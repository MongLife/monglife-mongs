package com.monglife.mongs.adapter.out.mong.schedule.repository;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.repository.dsl.TaskDslRepository;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskDslRepository {

    Optional<TaskEntity> findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCode(String appPackageName, String taskOwnerId, MongSchedulerTypeCode schedulerTypeCode);
}
