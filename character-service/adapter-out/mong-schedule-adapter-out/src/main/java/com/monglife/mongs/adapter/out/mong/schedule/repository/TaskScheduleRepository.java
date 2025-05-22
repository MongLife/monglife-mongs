package com.monglife.mongs.adapter.out.mong.schedule.repository;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskScheduleEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class TaskScheduleRepository {

    private static final Map<Long, TaskScheduleEntity> TASK_SCHEDULE_ENTITY_MAP = new ConcurrentHashMap<>();

    public Optional<TaskScheduleEntity> findByTaskId(Long taskId) {
        return Optional.ofNullable(TASK_SCHEDULE_ENTITY_MAP.get(taskId));
    }

    public Integer count() {
        return TASK_SCHEDULE_ENTITY_MAP.size();
    }

    public TaskScheduleEntity save(TaskScheduleEntity taskScheduleEntity) {

        if (TASK_SCHEDULE_ENTITY_MAP.containsKey(taskScheduleEntity.getTaskId())) {
            TASK_SCHEDULE_ENTITY_MAP.get(taskScheduleEntity.getTaskId()).stop();
        }

        TASK_SCHEDULE_ENTITY_MAP.put(taskScheduleEntity.getTaskId(), taskScheduleEntity);

        return TASK_SCHEDULE_ENTITY_MAP.get(taskScheduleEntity.getTaskId());
    }

    public void deleteByTaskId(Long taskId) {
        Optional.ofNullable(TASK_SCHEDULE_ENTITY_MAP.get(taskId))
                .ifPresent(taskScheduleEntity -> TASK_SCHEDULE_ENTITY_MAP.remove(taskScheduleEntity.getTaskId()));
    }

    public void deleteAll() {
        TASK_SCHEDULE_ENTITY_MAP.values().forEach(TaskScheduleEntity::stop);
        TASK_SCHEDULE_ENTITY_MAP.clear();
    }
}
