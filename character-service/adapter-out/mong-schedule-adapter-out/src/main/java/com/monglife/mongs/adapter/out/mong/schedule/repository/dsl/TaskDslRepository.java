package com.monglife.mongs.adapter.out.mong.schedule.repository.dsl;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

public interface TaskDslRepository {

    Optional<TaskEntity> findByTaskIdWithLock(Long taskId);

    List<TaskEntity> findAllByAppPackageNameWithLock(String appPackageName);

    List<TaskEntity> findAllByAppPackageNameAndMongIdWithLock(String appPackageName, Long mongId);

    Optional<TaskEntity> findByAppPackageNameAndMongIdAndSchedulerTypeCodeWithLock(String appPackageName, Long mongId, String schedulerTypeCode);
}
