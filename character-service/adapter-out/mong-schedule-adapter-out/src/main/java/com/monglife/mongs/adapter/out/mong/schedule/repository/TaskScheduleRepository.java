package com.monglife.mongs.adapter.out.mong.schedule.repository;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskScheduleEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskScheduleRepository {

    private static final Map<Long, TaskScheduleEntity> schedulerMap = new ConcurrentHashMap<>();

    public Set<Long> findTaskIdAll() {
        return schedulerMap.keySet();
    }

    public Optional<TaskScheduleEntity> findByTaskId(Long taskId) {
        return Optional.ofNullable(schedulerMap.get(taskId));
    }

    public List<TaskScheduleEntity> findAll() {
        return new ArrayList<>(schedulerMap.values());
    }

    public TaskScheduleEntity save(TaskScheduleEntity taskScheduleEntity) {
        return schedulerMap.put(taskScheduleEntity.getTaskId(), taskScheduleEntity);
    }

    public void stopAndDeleteByTaskId(Long taskId) {
        Optional.ofNullable(schedulerMap.get(taskId))
                .ifPresent(taskScheduleEntity -> {
                    taskScheduleEntity.stop();
                    schedulerMap.remove(taskScheduleEntity.getTaskId(), taskScheduleEntity);
                });
    }

    public void deleteByTaskId(Long taskId) {
        Optional.ofNullable(schedulerMap.get(taskId))
                .ifPresent(taskScheduleEntity -> {
                    schedulerMap.remove(taskScheduleEntity.getTaskId(), taskScheduleEntity);
                });
    }
}
