package com.mongs.play.module.task.repository;

import com.mongs.play.module.task.entity.TaskEvent;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskEventRepository extends CrudRepository<TaskEvent, String> {
    List<TaskEvent> findByTaskStatus(TaskStatus taskStatus);
    List<TaskEvent> findByMongIdAndTaskStatus(Long mongId, TaskStatus taskStatus);
    Optional<TaskEvent> findByMongIdAndTaskCodeAndTaskStatus(Long mongId, TaskCode taskCode, TaskStatus taskStatus);
}