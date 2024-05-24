package com.mongs.play.module.task.repository;

import com.mongs.play.module.task.entity.TaskEvent;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskEventRepository extends MongoRepository<TaskEvent, String> {
    List<TaskEvent> findByTaskStatus(TaskStatus taskStatus);
    List<TaskEvent> findByMongIdAndTaskStatusIsNot(Long mongId, TaskStatus taskStatus);
    Optional<TaskEvent> findByMongIdAndTaskCodeAndTaskStatus(Long mongId, TaskCode taskCode, TaskStatus taskStatus);
    Optional<TaskEvent> findByMongIdAndTaskCodeAndTaskStatusIn(Long mongId, TaskCode taskCode, List<TaskStatus> taskStatus);
}
