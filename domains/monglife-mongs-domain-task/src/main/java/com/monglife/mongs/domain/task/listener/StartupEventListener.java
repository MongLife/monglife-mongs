package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.BasicMarker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(-9999)
@Component
@RequiredArgsConstructor
public class StartupEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        if (applicationReadyEvent.getApplicationContext().getParent() == null) {

            String applicationName = applicationReadyEvent.getApplicationContext().getId();

            StringBuilder sb = new StringBuilder();
            taskService.appStopResumeAllTask(APP_PACKAGE_NAME).forEach(getTaskDto -> {
                sb.append("\n")
                        .append("[")
                        .append(getTaskDto.getTaskId())
                        .append("]")
                        .append(getTaskDto.getTaskOwnerId())
                        .append(" | ")
                        .append(getTaskDto.getTaskCode())
                        .append(" | ")
                        .append(getTaskDto.getRestExpirationSeconds())
                        .append("/")
                        .append(getTaskDto.getExpirationSeconds())
                        .append("(")
                        .append(getTaskDto.getExpiredAt())
                        .append(")")
                        .append(" | ")
                        .append(getTaskDto.getIsCycle() ? "cycle" : "")
                ;
            });

            log.info("\n[TASK LOAD ON \"{}\"] {}", applicationName, sb);
        }
    }
}
