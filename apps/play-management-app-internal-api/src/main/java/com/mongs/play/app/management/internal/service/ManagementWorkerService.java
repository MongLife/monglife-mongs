package com.mongs.play.app.management.internal.service;

import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementWorkerService {

    private final TaskService taskService;

    public void zeroEvolution(Long mongId) {
        taskService.startTask(mongId, TaskCode.ZERO_EVOLUTION);
    }

    public void firstEvolution(Long mongId) {
        taskService.startTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.startTask(mongId, TaskCode.INCREASE_POOP_COUNT);
    }

    public void evolution(Long mongId) {}

    public void lastEvolution(Long mongId) {
        taskService.forceStopAllTask(mongId);
    }

    public void sleepSleeping(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.stopTask(mongId, TaskCode.INCREASE_POOP_COUNT);

        taskService.pauseTask(mongId, TaskCode.DEAD_HEALTHY);
        taskService.pauseTask(mongId, TaskCode.DEAD_SATIETY);

        taskService.startTask(mongId, TaskCode.INCREASE_STATUS);
    }

    public void awakeSleeping(Long mongId) {
        taskService.stopTask(mongId, TaskCode.INCREASE_STATUS);

        taskService.startTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.startTask(mongId, TaskCode.INCREASE_POOP_COUNT);

        taskService.resumeTask(mongId, TaskCode.DEAD_HEALTHY);
        taskService.resumeTask(mongId, TaskCode.DEAD_SATIETY);
    }

    public void delete(Long mongId) {
        taskService.forceStopAllTask(mongId);
    }

    public void deadHealthy(Long mongId) {
        taskService.startTask(mongId, TaskCode.DEAD_HEALTHY);
    }

    public void liveHealthy(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DEAD_HEALTHY);
    }

    public void deadSatiety(Long mongId) {
        taskService.startTask(mongId, TaskCode.DEAD_SATIETY);
    }

    public void liveSatiety(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DEAD_SATIETY);
    }
}
