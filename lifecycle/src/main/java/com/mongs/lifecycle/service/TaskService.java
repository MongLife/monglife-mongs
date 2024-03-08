package com.mongs.lifecycle.service;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.EventTaskRepository;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.task.*;
import com.mongs.lifecycle.vo.StartTaskVo;
import com.mongs.lifecycle.vo.StopAllTaskVo;
import com.mongs.lifecycle.vo.StopTaskVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskActiveService taskActiveService;
    private final EventTaskRepository eventTaskRepository;
    private final MongEventRepository mongEventRepository;

    private final Integer MIN_POOP_EXPIRATION = 5;//60 * 60 * 4;


    @EventListener
    @Transactional
    public void startTask(StartTaskVo startTaskVo) {
        this.startTask(startTaskVo.mongId(), startTaskVo.mongEventCode());
    }
    @EventListener
    @Transactional
    public void stopTask(StopTaskVo stopTaskVo) {
        this.stopTask(stopTaskVo.mongId(), stopTaskVo.mongEventCode());
    }
    @EventListener
    @Transactional
    public void stopAllTask(StopAllTaskVo stopAllTaskVo) {
        this.stopAllTask(stopAllTaskVo.mongId());
    }


    private String getEventId(Long mongId, MongEventCode mongEventCode) {
        return mongId + "_" + mongEventCode.name();
    }

    private MongEvent generateMongEvent(Long mongId, MongEventCode mongEventCode) {
        long expiration = mongEventCode.getExpiration();

        if (MongEventCode.POOP.equals(mongEventCode)) {
            expiration = (int) (Math.random() * (expiration - MIN_POOP_EXPIRATION + 1) + MIN_POOP_EXPIRATION);
        }

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusSeconds(expiration);

        return MongEvent.builder()
                .eventId(getEventId(mongId, mongEventCode))
                .mongId(mongId)
                .eventCode(mongEventCode)
                .expiration(expiration)
                .expiredAt(expiredAt)
                .createdAt(createdAt)
                .statusCode(EventStatusCode.WAIT)
                .build();
    }

    public boolean isActiveTask(Long mongId, MongEventCode mongEventCode) {
        String eventId = getEventId(mongId, mongEventCode);
        return eventTaskRepository.findById(eventId).isPresent();
    }
    @Transactional
    public void startTask(Long mongId, MongEventCode mongEventCode) throws EventTaskException {
        try {
            MongEvent event = generateMongEvent(mongId, mongEventCode);

            if (isActiveTask(mongId, mongEventCode)) {
                throw new EventTaskException(LifecycleErrorCode.EXIST_TASK);
            }

            BasicTask basicTask =  switch (mongEventCode) {
                case WEIGHT_DOWN -> WeightDownTask.of(this, taskActiveService, event);
                case STRENGTH_DOWN -> StrengthDownTask.of(this, taskActiveService, event);
                case SATIETY_DOWN -> SatietyDownTask.of(this, taskActiveService, event);
                case HEALTHY_DOWN -> HealthyDownTask.of(this, taskActiveService, event);
                case SLEEP_DOWN -> SleepDownTask.of(this, taskActiveService, event);
                case SLEEP_UP -> SleepUpTask.of(this, taskActiveService, event);
                case PAY_POINT_UP -> PayPointUpTask.of(this, taskActiveService, event);
                case POOP -> GeneratePoopTask.of(this, taskActiveService, event);
                case DEAD_SATIETY, DEAD_HEALTHY, DEAD ->
                        DeadTask.of(this, taskActiveService, event);
            };

            if (basicTask == null) {
                throw new EventTaskException(LifecycleErrorCode.GENERATE_TASK_ERROR);
            }

            /* mongoDB 에 Event 저장 */
            mongEventRepository.save(event);
            /* Task 정보 저장 (삭제용) */
            eventTaskRepository.save(basicTask);
            /* Task 시작 */
            basicTask.start();

        } catch (RuntimeException e) {
            e.printStackTrace();
            log.info("[startTask] [{}] {} Task 진행중", mongId, mongEventCode);
        }
    }

    @Transactional
    public void stopTask(Long mongId, MongEventCode mongEventCode) throws EventTaskException {
        try {

            String eventId = getEventId(mongId, mongEventCode);
            /* Task 중지 */
            BasicTask basicTask = eventTaskRepository.findById(eventId)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));
            basicTask.stop();

            log.info("[stopTask] [{}] {} Task 삭제 ", mongEventCode, mongEventCode);

        } catch (RuntimeException e) {
            e.printStackTrace();
            log.info("[stopTask] [{}] {} Task 진행중이지 않음", mongId, mongEventCode);
        }
    }

    @Transactional
    public void stopAllTask(Long mongId) throws EventTaskException {
        try {
            mongEventRepository.findByMongIdAndStatusCode(mongId, EventStatusCode.WAIT)
                    .forEach(event -> this.stopTask(mongId, event.getEventCode()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("[stopAllTask] [{}] 모든 Task 중지 실패", mongId);
            throw new EventTaskException(LifecycleErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void modifyTaskStatus(String eventId, EventStatusCode statusCode, MongEventCode eventCode) throws EventTaskException {
        try {
            MongEvent mongEvent = mongEventRepository.findByIdAndStatusCodeNot(eventId, EventStatusCode.DONE)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

            mongEventRepository.save(mongEvent.toBuilder().statusCode(statusCode).build());

        } catch (EventTaskException e) {
            log.info("[stopTask] [{}] {} Task 진행중이지 않음", eventId, eventCode);
        } catch (RuntimeException e) {
            log.error("[modifyTaskStatus] [{}] {} 이벤트 상태 코드 변경 실패", eventId, eventCode);
        }
    }

    @Transactional
    public void deleteTask(String eventId) {
        eventTaskRepository.deleteById(eventId);
    }
}
