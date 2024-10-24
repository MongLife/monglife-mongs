package com.mongs.play.config;

import com.mongs.play.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(999)
@Component
@RequiredArgsConstructor
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

    private final TaskService taskService;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("#######################     종료 준비 (Task 일시 중지)     #######################");
        taskService.pauseSystemTask();
        log.info("####################### 종료 준비 완료 (Task 일시 중지 완료) #######################");
    }
}
