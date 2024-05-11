package com.mongs.play.module.code.service;

import com.mongs.play.core.exception.common.AlreadyExistException;
import com.mongs.play.module.code.entity.TaskEvent;
import com.mongs.play.module.code.enums.TaskCode;
import com.mongs.play.module.code.enums.TaskStatusCode;
import com.mongs.play.module.code.repository.TaskEventRepository;
import com.mongs.play.module.code.task.BasicTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final Map<String, BasicTask> taskMap = new ConcurrentHashMap<>();

    private final TaskEventRepository taskEventRepository;

    public void startTask(TaskEvent taskEvent) {

    }

    public void restartTask(TaskEvent taskEvent) {

    }

    public void restartAllTask() {

    }

    public void pauseTask(TaskEvent taskEvent) {

    }

    public void pauseAllTask() {

    }

    public void stopTask() {

    }

    public void stopAllTask() {

    }
}
