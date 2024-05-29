package com.mongs.play.app.management.internal.event;

import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.event.TaskPauseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskPauseEventListener {

    @Async
    @EventListener
    public void taskPauseEventListener(TaskPauseEvent event) {

        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();

        log.info("[taskPauseEventListener] mongId: {}, taskCode: {}", mongId, taskCode);
    }
}
