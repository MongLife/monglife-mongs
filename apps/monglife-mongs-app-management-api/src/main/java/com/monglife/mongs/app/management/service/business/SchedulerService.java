package com.monglife.mongs.app.management.service.business;

import com.mongs.play.domain.task.enums.TaskCode;
import com.mongs.play.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final TaskService taskService;
    private final ManagementInternalService managementInternalService;

    @Transactional
    public void zeroEvolution(Long mongId) {
        taskService.startTask(mongId, TaskCode.ZERO_EVOLUTION);
    }

    public void firstEvolution(Long mongId) {
        taskService.startTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.startTask(mongId, TaskCode.INCREASE_POOP_COUNT);
    }

    @Transactional
    public void evolution(Long mongId) {}

    @Transactional
    public void lastEvolution(Long mongId) {
        taskService.forceStopAllTask(mongId);
    }

    @Transactional
    public void sleepSleeping(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.stopTask(mongId, TaskCode.INCREASE_POOP_COUNT);

        taskService.pauseTask(mongId, TaskCode.DEAD);

        taskService.startTask(mongId, TaskCode.INCREASE_STATUS);
    }

    @Transactional
    public void awakeSleeping(Long mongId) {
        taskService.stopTask(mongId, TaskCode.INCREASE_STATUS);

        taskService.startTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.startTask(mongId, TaskCode.INCREASE_POOP_COUNT);

        taskService.resumeTask(mongId, TaskCode.DEAD);
    }

    @Transactional
    public void delete(Long mongId) {
        taskService.forceStopAllTask(mongId);
    }

    @Transactional
    public void dead(Long mongId) {
        taskService.startTask(mongId, TaskCode.DEAD);
        managementInternalService.toggleIsDeadSchedule(mongId, true);
    }

    @Transactional
    public void live(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DEAD);
        managementInternalService.toggleIsDeadSchedule(mongId, false);
    }
}
