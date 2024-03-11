package com.mongs.lifecycle.service;

import com.mongs.lifecycle.code.TaskCode;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LifecycleService {
    private final TaskService taskService;
    private final TaskActiveService taskActiveService;
    private final MongRepository mongRepository;

    public void graduationEvent(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<TaskCode> startList = List.of();
        List<TaskCode> restartList = List.of();
        List<TaskCode> pauseList = List.of();
        List<TaskCode> stopList = List.of(
                TaskCode.WEIGHT_DOWN,
                TaskCode.HEALTHY_DOWN,
                TaskCode.STRENGTH_DOWN,
                TaskCode.SATIETY_DOWN,
                TaskCode.SLEEP_DOWN,
                TaskCode.POOP,
                TaskCode.SLEEP_UP,
                TaskCode.PAY_POINT_UP,
                TaskCode.DEAD_HEALTHY,
                TaskCode.DEAD_SATIETY
        );

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void evolutionReadyEvent(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<TaskCode> startList = List.of();
        List<TaskCode> restartList = List.of();
        List<TaskCode> pauseList = List.of(
                TaskCode.DEAD_SATIETY,
                TaskCode.DEAD_HEALTHY
        );
        List<TaskCode> stopList = List.of(
                TaskCode.WEIGHT_DOWN,
                TaskCode.HEALTHY_DOWN,
                TaskCode.STRENGTH_DOWN,
                TaskCode.SATIETY_DOWN,
                TaskCode.SLEEP_DOWN,
                TaskCode.POOP
        );

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void evolutionEvent(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<TaskCode> startList = List.of(
                TaskCode.WEIGHT_DOWN,
                TaskCode.HEALTHY_DOWN,
                TaskCode.STRENGTH_DOWN,
                TaskCode.SATIETY_DOWN,
                TaskCode.SLEEP_DOWN,
                TaskCode.POOP
        );
        List<TaskCode> restartList = List.of(
                TaskCode.DEAD_SATIETY,
                TaskCode.DEAD_HEALTHY
        );
        List<TaskCode> pauseList = List.of();
        List<TaskCode> stopList = List.of();

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void sleepEvent(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<TaskCode> startList = List.of(
                TaskCode.SLEEP_UP
        );
        List<TaskCode> restartList = List.of();
        List<TaskCode> pauseList = List.of();
        List<TaskCode> stopList = List.of(
                TaskCode.WEIGHT_DOWN,
                TaskCode.HEALTHY_DOWN,
                TaskCode.STRENGTH_DOWN,
                TaskCode.SATIETY_DOWN,
                TaskCode.SLEEP_DOWN,
                TaskCode.POOP
        );

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void wakeupEvent(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<TaskCode> startList = List.of(
                TaskCode.WEIGHT_DOWN,
                TaskCode.HEALTHY_DOWN,
                TaskCode.STRENGTH_DOWN,
                TaskCode.SATIETY_DOWN,
                TaskCode.SLEEP_DOWN,
                TaskCode.POOP
        );
        List<TaskCode> restartList = List.of();
        List<TaskCode> pauseList = List.of();
        List<TaskCode> stopList = List.of(
                TaskCode.SLEEP_UP
        );

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void dead(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }
        taskActiveService.dead(mongId, TaskCode.DEAD);
        taskService.stopAllTask(mongId);
    }

    private void exec(
            Long mongId,
            List<TaskCode> startList,
            List<TaskCode> restartList,
            List<TaskCode> pauseList,
            List<TaskCode> stopList
    ) {
        startList.forEach(eventCode -> {
            try {
                taskService.startTask(mongId, eventCode);
            } catch (EventTaskException e) {
                log.error("[exec] [{}] {} 시작 실패", mongId, eventCode);
            }
        });
        restartList.forEach(eventCode -> {
            try {
                taskService.restartTask(mongId, eventCode);
            } catch (EventTaskException e) {
                log.error("[exec] [{}] {} 재시작 실패", mongId, eventCode);
            }
        });
        pauseList.forEach(eventCode -> {
            try {
                taskService.pauseTask(mongId, eventCode);
            } catch (EventTaskException e) {
                log.error("[exec] [{}] {} 일시중지 실패", mongId, eventCode);
            }
        });
        stopList.forEach(eventCode -> {
            try {
                taskService.stopTask(mongId, eventCode);
            } catch (EventTaskException e) {
                log.error("[exec] [{}] {} 종료 실패", mongId, eventCode);
            }

        });
    }
}
