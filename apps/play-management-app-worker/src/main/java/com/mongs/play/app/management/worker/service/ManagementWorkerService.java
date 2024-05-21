package com.mongs.play.app.management.worker.service;

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

    @Transactional
    public void zeroEvolution(Long mongId) {
        taskService.startTask(mongId, TaskCode.ZERO_EVOLUTION);
    }

    @Transactional
    public void firstEvolution(Long mongId) {
        taskService.startTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.startTask(mongId, TaskCode.INCREASE_POOP_COUNT);
    }

    @Transactional
    public void evolution(Long mongId) {
    }

    @Transactional
    public void lastEvolution(Long mongId) {
        taskService.stopAllTask(mongId);
    }

    @Transactional
    public void sleepSleeping(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.stopTask(mongId, TaskCode.INCREASE_POOP_COUNT);

        taskService.pauseTask(mongId, TaskCode.DEAD_HEALTHY);
        taskService.pauseTask(mongId, TaskCode.DEAD_SATIETY);

        taskService.startTask(mongId, TaskCode.INCREASE_STATUS);
    }

    @Transactional
    public void awakeSleeping(Long mongId) {
        taskService.stopTask(mongId, TaskCode.INCREASE_STATUS);

        taskService.startTask(mongId, TaskCode.DECREASE_STATUS);
        taskService.startTask(mongId, TaskCode.INCREASE_POOP_COUNT);

        taskService.resumeTask(mongId, TaskCode.DEAD_HEALTHY);
        taskService.resumeTask(mongId, TaskCode.DEAD_SATIETY);
    }

    @Transactional
    public void delete(Long mongId) {
        taskService.stopAllTask(mongId);
    }

    @Transactional
    public void deadHealthy(Long mongId) {
        taskService.startTask(mongId, TaskCode.DEAD_SATIETY);
    }

    @Transactional
    public void liveHealthy(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DEAD_SATIETY);
    }

    @Transactional
    public void deadSatiety(Long mongId) {
        taskService.startTask(mongId, TaskCode.DEAD_HEALTHY);
    }

    @Transactional
    public void liveSatiety(Long mongId) {
        taskService.stopTask(mongId, TaskCode.DEAD_HEALTHY);
    }
}
