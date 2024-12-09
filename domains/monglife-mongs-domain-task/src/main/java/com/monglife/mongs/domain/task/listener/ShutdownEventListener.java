package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Order(-9999)
@Component
@RequiredArgsConstructor
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

    @Value("${application.app-code}")
    private String APP_CODE;

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {

        String applicationName = contextClosedEvent.getApplicationContext().getApplicationName();

        log.info("[TASK STOP WORKING START] {}", applicationName);

        StringBuilder sb = new StringBuilder();
        taskService.appStopPauseAllTask(APP_CODE).forEach(getTaskDto -> {
            sb.append("\n").append(getTaskDto.getTaskId()).append(" =====> ").append(getTaskDto.getTaskOwnerId()).append(" : ").append(getTaskDto.getTaskCode());
        });

        log.info("[TASK STOP WORKING SUCCESS] {}{}", applicationName, sb);
    }
}