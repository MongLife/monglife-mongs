package com.mongs.play.config;

import com.mongs.play.app.management.internal.service.ManagementWorkerService;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(-999)
@Component
@RequiredArgsConstructor
public class StartupEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final TaskService taskService;
    private final MongService mongService;
    private final ManagementWorkerService managementWorkerService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("#######################     시작 준비 (Task 재시작)     #######################");
        taskService.resumeSystemTask();
//        mongService.findAllMong().forEach(mongVo -> {
//            if (MongGrade.ZERO.equals(mongVo.grade()) && !MongShift.EVOLUTION_READY.equals(mongVo.shift())) {
//                managementWorkerService.zeroEvolution(mongVo.mongId());
//            } else if (MongGrade.LAST.equals(mongVo.grade())) {
//                managementWorkerService.lastEvolution(mongVo.mongId());
//            } else if (mongVo.isSleeping()) {
//                managementWorkerService.sleepSleeping(mongVo.mongId());
//            } else {
//                managementWorkerService.awakeSleeping(mongVo.mongId());
//            }
//        });
        log.info("####################### 시작 준비 완료 (Task 재시작 완료) #######################");
    }
}
