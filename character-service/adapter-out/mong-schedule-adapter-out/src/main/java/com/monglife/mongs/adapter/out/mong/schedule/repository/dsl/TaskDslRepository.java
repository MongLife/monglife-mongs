package com.monglife.mongs.adapter.out.mong.schedule.repository.dsl;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;

import java.util.List;
import java.util.Optional;

public interface TaskDslRepository {

    Optional<TaskEntity> findByTaskIdWithLock(Long taskId);

    List<TaskEntity> findAllByAppPackageNameWithLock(String appPackageName);

    List<TaskEntity> findAllByAppPackageNameAndTaskOwnerIdWithLock(String appPackageName, String taskOwnerId);

    Optional<TaskEntity> findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCodeWithLock(String appPackageName, String taskOwnerId, MongSchedulerTypeCode schedulerTypeCode);
}
