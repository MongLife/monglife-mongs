package com.monglife.mongs.adapter.out.mong.schedule.listener;

import com.monglife.mongs.adapter.out.mong.schedule.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(Integer.MIN_VALUE)
@Component
@RequiredArgsConstructor
public class StartupEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        if (applicationReadyEvent.getApplicationContext().getParent() == null) {

            String applicationName = applicationReadyEvent.getApplicationContext().getId();

            StringBuilder sb = new StringBuilder();
            taskService.appStopResumeAllTask().forEach(taskEntity -> {
                sb.append("\n")
                        .append("[")
                        .append(taskEntity.getTaskId())
                        .append("]")
                        .append(taskEntity.getTaskOwnerId())
                        .append(" | ")
                        .append(taskEntity.getSchedulerTypeCode())
                        .append(" | ")
                        .append(taskEntity.getRestExpirationSeconds())
                        .append("/")
                        .append(taskEntity.getExpirationSeconds())
                        .append("(")
                        .append(taskEntity.getExpiredAt())
                        .append(")")
                ;
            });

            log.info("\n[TASK LOAD ON \"{}\"] {}", applicationName, sb);
        }
    }
}
