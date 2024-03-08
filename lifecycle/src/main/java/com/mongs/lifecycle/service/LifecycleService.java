package com.mongs.lifecycle.service;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
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
            MongEventRepository mongEventRepository,
            MongRepository mongRepository
    ) {
        this.taskService = taskService;
        this.mongRepository = mongRepository;
        mongEventRepository.deleteAll();
        for (long mongId = 1; mongId <= 1000; mongId++) {
            mongRepository.save(Mong.builder()
                    .accountId(1L)
                    .name("(" + mongId + ") 테스트 몽")
                    .healthy(10D)
                    .satiety(20D)
                    .build());
        }
    }

    public void stressTestEvent(Long mongId) {
//        taskService.startTask(mongId, MongEventCode.WEIGHT_DOWN);
        taskService.startTask(mongId, MongEventCode.HEALTHY_DOWN);
//        taskService.startTask(mongId, MongEventCode.STRENGTH_DOWN);
        taskService.startTask(mongId, MongEventCode.SATIETY_DOWN);
//        taskService.startTask(mongId, MongEventCode.SLEEP_DOWN);
//        taskService.startTask(mongId, MongEventCode.POOP);
    }

    public void evolutionEvent(Long mongId) {
        if (mongRepository.findAllByIdAndIsActiveFalse(mongId).isPresent()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<MongEventCode> startList = List.of(
                MongEventCode.WEIGHT_DOWN,
                MongEventCode.HEALTHY_DOWN,
                MongEventCode.STRENGTH_DOWN,
                MongEventCode.SATIETY_DOWN,
                MongEventCode.SLEEP_DOWN,
                MongEventCode.POOP
        );
        List<MongEventCode> stopList = List.of(
        );

        exec(mongId, startList, stopList);
    }

    public void sleepEvent(Long mongId) {
        if (mongRepository.findAllByIdAndIsActiveFalse(mongId).isPresent()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<MongEventCode> startList = List.of(
                MongEventCode.SLEEP_UP
        );
        List<MongEventCode> stopList = List.of(
                MongEventCode.WEIGHT_DOWN,
                MongEventCode.HEALTHY_DOWN,
                MongEventCode.STRENGTH_DOWN,
                MongEventCode.SATIETY_DOWN,
                MongEventCode.SLEEP_DOWN,
                MongEventCode.POOP
        );

        exec(mongId, startList, stopList);
    }

    public void wakeupEvent(Long mongId) {
        if (mongRepository.findAllByIdAndIsActiveFalse(mongId).isPresent()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        List<MongEventCode> startList = List.of(
                MongEventCode.WEIGHT_DOWN,
                MongEventCode.HEALTHY_DOWN,
                MongEventCode.STRENGTH_DOWN,
                MongEventCode.SATIETY_DOWN,
                MongEventCode.SLEEP_DOWN,
                MongEventCode.POOP
        );
        List<MongEventCode> stopList = List.of(
                MongEventCode.SLEEP_UP
        );

        exec(mongId, startList, stopList);
    }

    public void dead(Long mongId) {
        if (mongRepository.findAllByIdAndIsActiveFalse(mongId).isPresent()) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
        }

        // TODO("MongActiveService.dead() 실행 필요")
        taskService.stopAllTask(mongId);
    }

    private void exec(
            Long mongId,
            List<MongEventCode> startList,
            List<MongEventCode> stopList
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
