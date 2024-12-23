package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(-9999)
@Component
@RequiredArgsConstructor
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {

        String applicationName = contextClosedEvent.getApplicationContext().getApplicationName();

        log.info("[TASK STOP WORKING START] {}", applicationName);

        StringBuilder sb = new StringBuilder();
        taskService.appStopPauseAllTask(APP_PACKAGE_NAME).forEach(getTaskDto -> {
            sb.append("\n").append(getTaskDto.getTaskId()).append(" =====> ").append(getTaskDto.getTaskOwnerId()).append(" : ").append(getTaskDto.getTaskCode());
        });

        log.info("[TASK STOP WORKING SUCCESS] {}{}", applicationName, sb);
    }
}