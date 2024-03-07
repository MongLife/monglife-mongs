package com.mongs.lifecycle.service_;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.EventTaskRepository;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.thread.BasicTask;
import com.mongs.lifecycle.thread.HealthyDownTask;
import com.mongs.lifecycle.utils.MongEventUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final MongEventUtil mongEventUtil;
    private final TaskActiveService taskActiveService;
    private final EventTaskRepository eventTaskRepository;
    private final MongEventRepository mongEventRepository;

    public void registerTask(Long mongId, MongEventCode mongEventCode) {
        try {
            BasicTask basicTask = HealthyDownTask.of(
                    this, taskActiveService, mongEventUtil.generateMongEvent(mongId, mongEventCode));

            mongEventRepository.save(basicTask.getEvent());
            eventTaskRepository.save(basicTask);
            basicTask.start();
        } catch (RuntimeException e) {
            log.error("[registerTask] {} : {} Task 재실행 실패", mongId, MongEventCode.HEALTHY_DOWN);
        }
    }

    public void deleteTask() {
        // TODO("Task 삭제 로직")
    }

    public void modifyTaskStatus(String eventId, EventStatusCode statusCode) {
        MongEvent mongEvent = mongEventRepository.findById(eventId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));
        mongEventRepository.save(mongEvent.toBuilder().statusCode(statusCode).build());
    }
}
