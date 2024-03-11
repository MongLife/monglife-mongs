package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.code.TaskCode;
import com.mongs.lifecycle.code.TaskStatusCode;
import com.mongs.lifecycle.entity.TaskEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskEventRepository extends MongoRepository<TaskEvent, String> {
    Optional<TaskEvent> findByTaskIdAndStatusCode(String taskId, TaskStatusCode taskStatusCode);
    Optional<TaskEvent> findByMongIdAndTaskCodeAndStatusCodeIn(Long mongId, TaskCode taskCode, List<TaskStatusCode> taskStatusCode);
    List<TaskEvent> findAllByMongIdAndStatusCode(Long mongId, TaskStatusCode taskStatusCode);
    Optional<TaskEvent> findByMongIdAndTaskCodeAndStatusCode(Long mongId, TaskCode taskCode, TaskStatusCode taskStatusCode);
    List<TaskEvent> findAllByStatusCode(TaskStatusCode taskStatusCode);
}
