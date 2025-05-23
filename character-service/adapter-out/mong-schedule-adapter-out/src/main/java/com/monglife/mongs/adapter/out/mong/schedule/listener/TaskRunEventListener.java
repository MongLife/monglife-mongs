package com.monglife.mongs.adapter.out.mong.schedule.listener;

import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskScheduleEntity;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskRepository;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskScheduleRepository;
import com.monglife.mongs.adapter.transaction.*;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerType;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

@Component
@RequiredArgsConstructor
public class TaskRunEventListener {

    private final TaskRepository taskRepository;

    private final TaskScheduleRepository taskScheduleRepository;

    private final KafkaService kafkaService;

    private final ApplicationEventPublisher publisher;

    private final ScheduledExecutorService executor;

    /**
     * 테스크 스케줄 실행 이벤트 리스너
     * @param taskEntity 테스크 엔티티
     */
    @EventListener(TaskEntity.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void taskRunEventListener(TaskEntity taskEntity) {

        Long mongId = Long.parseLong(taskEntity.getTaskOwnerId());

        taskRepository.findByTaskIdWithLock(taskEntity.getTaskId()).ifPresent(pasTaskEntity -> {
            // 테스크 스케줄 삭제
            taskScheduleRepository.findByTaskId(taskEntity.getTaskId()).ifPresent(taskScheduleEntity -> {
                taskScheduleRepository.deleteByTaskId(taskScheduleEntity.getTaskId());
            });

            // 반복 테스크 경우 새로 생성
            if (pasTaskEntity.isCycle()) {
                pasTaskEntity.cycle();
                taskScheduleRepository.save(TaskScheduleEntity.of(pasTaskEntity))
                        .start(executor, () -> publisher.publishEvent(taskEntity));
            } else {
                taskRepository.delete(pasTaskEntity);
            }
        });

        Arrays.stream(MongSchedulerType.values())
                .filter(mongSchedulerType -> mongSchedulerType.getCode().equals(taskEntity.getSchedulerTypeCode()))
                .findFirst()
                .ifPresentOrElse(mongSchedulerType -> {
                    switch (mongSchedulerType) {
                        case EGG_EVOLUTION ->
                                kafkaService.generateEvent(EventTopic.COMMIT_EGG_EVOLUTION, EggEvolutionEventDto.builder()
                                        .mongId(mongId)
                                        .build());
                        case INCREASE_STATUS ->
                                kafkaService.generateEvent(EventTopic.COMMIT_INCREASE_STATUS, IncreaseStatusEventDto.builder()
                                        .mongId(mongId)
                                        .build());
                        case DECREASE_STATUS ->
                                kafkaService.generateEvent(EventTopic.COMMIT_DECREASE_STATUS, DecreaseStatusEventDto.builder()
                                        .mongId(mongId)
                                        .build());
                        case INCREASE_POOP ->
                                kafkaService.generateEvent(EventTopic.COMMIT_INCREASE_POOP, IncreasePoopEventDto.builder()
                                        .mongId(mongId)
                                        .build());
                        case DEAD -> kafkaService.generateEvent(EventTopic.COMMIT_DEAD, DeadEventDto.builder()
                                .mongId(mongId)
                                .build());
                        case SLEEP -> kafkaService.generateEvent(EventTopic.COMMIT_SLEEP, SleepEventDto.builder()
                                .mongId(mongId)
                                .build());
                        case WAKEUP -> kafkaService.generateEvent(EventTopic.COMMIT_WAKEUP, WakeupEventDto.builder()
                                .mongId(mongId)
                                .build());
                    }
                }, () -> kafkaService.generateEvent("commit.test", Map.of("taskId", taskEntity.getTaskId())));
    }
}
