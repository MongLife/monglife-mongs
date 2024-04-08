package com.mongs.lifecycle.service.componentService;

import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.lifecycle.TaskCode;
import com.mongs.lifecycle.entity.Mong;
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
    private final MongRepository mongRepository;
    private final TaskService taskService;

    public void eggEvent(Long mongId) {
        List<TaskCode> startList = List.of(TaskCode.EGG);
        List<TaskCode> restartList = List.of();
        List<TaskCode> pauseList = List.of();
        List<TaskCode> stopList = List.of();

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void eggEvolutionEvent(Long mongId, Long accountId) {
        if (mongRepository.findByIdAndAccountIdAndIsActiveTrue(mongId, accountId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<TaskCode> startList = List.of(TaskCode.WEIGHT_DOWN, TaskCode.HEALTHY_DOWN, TaskCode.STRENGTH_DOWN, TaskCode.SATIETY_DOWN, TaskCode.SLEEP_DOWN, TaskCode.POOP);
        List<TaskCode> restartList = List.of(TaskCode.DEAD_SATIETY, TaskCode.DEAD_HEALTHY);
        List<TaskCode> pauseList = List.of();
        List<TaskCode> stopList = List.of();

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void sleepEvent(Long mongId, Long accountId) {
        Mong mong = mongRepository.findByIdAndAccountIdAndIsActiveTrue(mongId, accountId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        List<TaskCode> startList = List.of(TaskCode.SLEEP_UP);
        List<TaskCode> restartList = List.of();
        List<TaskCode> pauseList;
        List<TaskCode> stopList;

        if (mong.getShift().equals(MongShift.GRADUATE_READY)) {
            stopList = List.of();
            pauseList = List.of();
        } else {
            stopList = List.of(TaskCode.WEIGHT_DOWN, TaskCode.HEALTHY_DOWN, TaskCode.STRENGTH_DOWN, TaskCode.SATIETY_DOWN, TaskCode.SLEEP_DOWN);
            pauseList = List.of(TaskCode.POOP);
        }

        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void wakeupEvent(Long mongId, Long accountId) {
        Mong mong = mongRepository.findByIdAndAccountIdAndIsActiveTrue(mongId, accountId)
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        List<TaskCode> startList;
        List<TaskCode> restartList;
        List<TaskCode> pauseList = List.of();
        List<TaskCode> stopList = List.of(TaskCode.SLEEP_UP);

        if (mong.getShift().equals(MongShift.GRADUATE_READY)) {
            startList = List.of();
            restartList = List.of();
        } else {
            startList = List.of(TaskCode.WEIGHT_DOWN, TaskCode.HEALTHY_DOWN, TaskCode.STRENGTH_DOWN, TaskCode.SATIETY_DOWN, TaskCode.SLEEP_DOWN);
            restartList = List.of(TaskCode.POOP);
        }
        exec(mongId, startList, restartList, pauseList, stopList);
    }

    public void graduationReadyEvent(Long mongId, Long accountId) {
        if (mongRepository.findByIdAndAccountIdAndIsActiveTrue(mongId, accountId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        taskService.stopAllTask(mongId);
    }

    public void deleteEvent(Long mongId, Long accountId) {
        if (mongRepository.findByIdAndAccountId(mongId, accountId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

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
