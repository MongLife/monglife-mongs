package com.mongs.lifecycle.service;

import com.mongs.lifecycle.code.TaskCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.TaskEventRepository;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
//@RequiredArgsConstructor
public class LifecycleService {
    private final TaskService taskService;
    private final MongRepository mongRepository;


    @Autowired
    public LifecycleService(
            TaskService taskService,
            TaskEventRepository taskEventRepository,
            MongRepository mongRepository
    ) {
        this.taskService = taskService;
        this.mongRepository = mongRepository;
        taskEventRepository.deleteAll();
        for (long mongId = 1; mongId <= 1000; mongId++) {
            mongRepository.save(Mong.builder()
                    .accountId(1L)
                    .name("(" + mongId + ") 테스트 몽")
//                    .healthy(5D)
//                    .satiety(10D)
                    .build());
        }
    }

    public void stressTestEvent(Long mongId) {
        taskService.startTask(mongId, TaskCode.WEIGHT_DOWN);
        taskService.startTask(mongId, TaskCode.HEALTHY_DOWN);
        taskService.startTask(mongId, TaskCode.STRENGTH_DOWN);
        taskService.startTask(mongId, TaskCode.SATIETY_DOWN);
        taskService.startTask(mongId, TaskCode.SLEEP_DOWN);
        taskService.startTask(mongId, TaskCode.POOP);
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
        List<TaskCode> stopList = List.of(
        );

        exec(mongId, startList, stopList);
    }

    public void sleepEvent(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<TaskCode> startList = List.of(
                TaskCode.SLEEP_UP
        );
        List<TaskCode> stopList = List.of(
                TaskCode.WEIGHT_DOWN,
                TaskCode.HEALTHY_DOWN,
                TaskCode.STRENGTH_DOWN,
                TaskCode.SATIETY_DOWN,
                TaskCode.SLEEP_DOWN,
                TaskCode.POOP
        );

        exec(mongId, startList, stopList);
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
        List<TaskCode> stopList = List.of(
                TaskCode.SLEEP_UP
        );

        exec(mongId, startList, stopList);
    }

    public void dead(Long mongId) {
        if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        // TODO("MongActiveService.dead() 실행 필요")
        taskService.stopAllTask(mongId);
    }

    private void exec(
            Long mongId,
            List<TaskCode> startList,
            List<TaskCode> stopList
    ) {
        startList.forEach(eventCode -> {
            try {
                taskService.startTask(mongId, eventCode);
            } catch (EventTaskException e) {
                log.error("[exec] [{}] {} 시작 실패", mongId, eventCode);
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
