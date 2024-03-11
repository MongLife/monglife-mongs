package com.mongs.lifecycle.config;

import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.repository.MongRepository;
import com.mongs.lifecycle.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private final TaskService taskService;
    private final MongRepository mongRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (long mongId = 1; mongId <= 1000; mongId++) {
            mongRepository.save(Mong.builder()
                    .accountId(1L)
                    .name("(" + mongId + ") 테스트 몽")
                    .build());
        }

        log.info("#######################     시작 준비 (Task 재시작)     #######################");
        taskService.restartAllTask();
        log.info("####################### 시작 준비 완료 (Task 재시작 완료) #######################");

    }
}
