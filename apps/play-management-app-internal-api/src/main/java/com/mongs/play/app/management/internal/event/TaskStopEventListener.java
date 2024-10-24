package com.mongs.play.app.management.internal.event;

import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.core.error.app.ManagementWorkerErrorCode;
import com.mongs.play.core.exception.app.ManagementWorkerException;
import com.mongs.play.domain.task.enums.TaskCode;
import com.mongs.play.domain.task.enums.TaskUtil;
import com.mongs.play.domain.task.event.TaskStopEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskStopEventListener {

    private final ManagementInternalService managementInternalService;

    @Async
    @EventListener
    public void taskStopEventListener(TaskStopEvent event) {

        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();
        Long expiration = event.getExpiration();
        Long duringSeconds = event.getDuringSeconds();

        switch (taskCode) {
            case ZERO_EVOLUTION -> {}
            case DECREASE_STATUS -> {
                double subWeight = TaskUtil.subWeight / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subStrength = TaskUtil.subStrength / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subSatiety = TaskUtil.subSatiety / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subHealthy = TaskUtil.subHealthy / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subSleep = TaskUtil.subSleep / TaskUtil.decreaseStatusExpiration * duringSeconds;

                managementInternalService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);
            }
            case INCREASE_STATUS -> {
                double addWeight = TaskUtil.addWeight / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addStrength = TaskUtil.addStrength / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addSatiety = TaskUtil.addSatiety / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addHealthy = TaskUtil.addHealthy / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addSleep = TaskUtil.addSleep / TaskUtil.increaseStatusExpiration * duringSeconds;

                managementInternalService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);
            }
            case INCREASE_POOP_COUNT -> {
                if ((double) (duringSeconds / expiration) > 0.5) {
                    int addPoopCount = 1;
                    managementInternalService.increasePoopCount(mongId, addPoopCount);
                }
            }
            case DEAD -> {}
            default -> throw new ManagementWorkerException(ManagementWorkerErrorCode.INVALID_PAUSE_EVENT);
        }
    }
}
