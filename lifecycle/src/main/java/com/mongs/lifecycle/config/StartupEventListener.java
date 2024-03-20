package com.mongs.lifecycle.config;

import com.mongs.lifecycle.service.componentService.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final TaskService taskService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("#######################     시작 준비 (Task 재시작)     #######################");
        taskService.restartAllTask();
        log.info("####################### 시작 준비 완료 (Task 재시작 완료) #######################");
    }
}
