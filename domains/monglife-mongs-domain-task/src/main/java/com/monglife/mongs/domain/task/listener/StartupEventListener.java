package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.etc.GetTaskDto;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@Order(-9999)
@Component
@RequiredArgsConstructor
public class StartupEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${application.app-code}")
    private String APP_CODE;

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        String applicationName = applicationReadyEvent.getApplicationContext().getApplicationName();

        StringBuilder sb = new StringBuilder();
        taskService.appStopResumeAllTask(APP_CODE).forEach(getTaskDto -> {
            sb.append("\n").append(getTaskDto.getTaskId()).append(" =====> ").append(getTaskDto.getTaskOwnerId()).append(" : ").append(getTaskDto.getTaskCode());
        });

        log.info("[TASK LOAD SUCCESS] {}{}", applicationName, sb);
    }
}
