package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Value("${application.app-code}")
    private String APP_CODE;

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("#######################      시작 준비 (Task 재시작)      #######################");
        applicationReadyEvent.getApplicationContext();
        taskService.appStopResumeAllTask(APP_CODE);
        log.info("####################### 시작 준비 완료 (Task 재시작 완료) #######################");
    }
}
