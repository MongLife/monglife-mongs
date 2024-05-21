package com.mongs.play.app.management.worker.event;

import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.event.TaskPauseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskPauseEventListener {

    @EventListener
    public void taskPauseEventListener(TaskPauseEvent event) {

        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();

        log.info("[taskPauseEventListener] mongId: {}, taskCode: {}", mongId, taskCode);
    }
}
