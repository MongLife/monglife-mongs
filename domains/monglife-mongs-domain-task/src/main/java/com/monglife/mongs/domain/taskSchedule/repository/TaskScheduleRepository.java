package com.monglife.mongs.domain.taskSchedule.repository;

import com.monglife.mongs.domain.taskSchedule.entity.TaskScheduleEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskScheduleRepository {

    private static final Map<Long, TaskScheduleEntity> schedulerMap = new ConcurrentHashMap<>();

    public Optional<TaskScheduleEntity> findByTaskId(Long taskId) {
        return Optional.ofNullable(schedulerMap.get(taskId));
    }

    public List<TaskScheduleEntity> findAll() {
        return new ArrayList<>(schedulerMap.values());
    }

    public TaskScheduleEntity save(TaskScheduleEntity taskScheduleEntity) {
        return schedulerMap.put(taskScheduleEntity.getTaskId(), taskScheduleEntity);
    }

    public void deleteByTaskId(Long taskId) {
        Optional.ofNullable(schedulerMap.get(taskId))
                .ifPresent(taskScheduleEntity -> {
                    taskScheduleEntity.stop();
                    schedulerMap.remove(taskScheduleEntity.getTaskId(), taskScheduleEntity);
                });
    }
}
