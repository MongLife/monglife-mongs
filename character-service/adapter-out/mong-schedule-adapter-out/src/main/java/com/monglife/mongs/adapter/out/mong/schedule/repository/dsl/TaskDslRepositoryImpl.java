package com.monglife.mongs.adapter.out.mong.schedule.repository.dsl;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.monglife.mongs.adapter.out.mong.schedule.entity.QTaskEntity.taskEntity;

@Repository
public class TaskDslRepositoryImpl implements TaskDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public TaskDslRepositoryImpl(@Qualifier("taskJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<TaskEntity> findByTaskIdWithLock(Long taskId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(taskEntity)
                .where(taskEntity.taskId.eq(taskId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }

    @Override
    public List<TaskEntity> findAllByAppPackageNameWithLock(String appPackageName) {
        return jpaQueryFactory.selectFrom(taskEntity)
                .where(taskEntity.appPackageName.eq(appPackageName))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();
    }

    @Override
    public List<TaskEntity> findAllByAppPackageNameAndTaskOwnerIdWithLock(String appPackageName, String taskOwnerId) {
        return jpaQueryFactory.selectFrom(taskEntity)
                .where(taskEntity.appPackageName.eq(appPackageName))
                .where(taskEntity.taskOwnerId.eq(taskOwnerId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();
    }

    @Override
    public Optional<TaskEntity> findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCodeWithLock(String appPackageName, String taskOwnerId, MongSchedulerTypeCode schedulerTypeCode) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(taskEntity)
                .where(taskEntity.appPackageName.eq(appPackageName))
                .where(taskEntity.taskOwnerId.eq(taskOwnerId))
                .where(taskEntity.schedulerTypeCode.eq(schedulerTypeCode))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
