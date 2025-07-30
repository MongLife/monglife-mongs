package com.monglife.mongs.adapter.out.mong.schedule.listener;

import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskScheduleEntity;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskRepository;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskScheduleRepository;
import com.monglife.mongs.adapter.transaction.*;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerType;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class TaskRunEventListener {

    private final TaskRepository taskRepository;

    private final TaskScheduleRepository taskScheduleRepository;

    private final KafkaService kafkaService;

    private final ApplicationEventPublisher publisher;

    private final ScheduledExecutorService executor;

    public TaskRunEventListener(
            @Autowired TaskRepository taskRepository,
            @Autowired TaskScheduleRepository taskScheduleRepository,
            @Autowired KafkaService kafkaService,
            @Autowired ApplicationEventPublisher publisher,
            @Qualifier("taskScheduledExecutorService") ScheduledExecutorService executor
    ) {
        this.taskRepository = taskRepository;
        this.taskScheduleRepository = taskScheduleRepository;
        this.kafkaService = kafkaService;
        this.publisher = publisher;
        this.executor = executor;
    }

    /**
     * 테스크 스케줄 실행 이벤트 리스너
     * @param taskEntity 테스크 엔티티
     */
    @EventListener(TaskEntity.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void taskRunEventListener(TaskEntity taskEntity) {

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
                                kafkaService.generateEventWithProfile(EventTopic.COMMIT_EGG_EVOLUTION, EggEvolutionEventDto.builder()
                                        .accountId(taskEntity.getAccountId())
                                        .mongId(taskEntity.getMongId())
                                        .build());
                        case INCREASE_STATUS ->
                                kafkaService.generateEventWithProfile(EventTopic.COMMIT_INCREASE_STATUS, IncreaseStatusEventDto.builder()
                                        .accountId(taskEntity.getAccountId())
                                        .mongId(taskEntity.getMongId())
                                        .build());
                        case DECREASE_STATUS ->
                                kafkaService.generateEventWithProfile(EventTopic.COMMIT_DECREASE_STATUS, DecreaseStatusEventDto.builder()
                                        .accountId(taskEntity.getAccountId())
                                        .mongId(taskEntity.getMongId())
                                        .build());
                        case INCREASE_POOP ->
                                kafkaService.generateEventWithProfile(EventTopic.COMMIT_INCREASE_POOP, IncreasePoopEventDto.builder()
                                        .accountId(taskEntity.getAccountId())
                                        .mongId(taskEntity.getMongId())
                                        .build());
                        case DEAD -> kafkaService.generateEventWithProfile(EventTopic.COMMIT_DEAD, DeadEventDto.builder()
                                        .accountId(taskEntity.getAccountId())
                                        .mongId(taskEntity.getMongId())
                                        .build());
                        case SLEEP -> kafkaService.generateEventWithProfile(EventTopic.COMMIT_SLEEP, SleepEventDto.builder()
                                        .accountId(taskEntity.getAccountId())
                                        .mongId(taskEntity.getMongId())
                                        .build());
                        case WAKEUP -> kafkaService.generateEventWithProfile(EventTopic.COMMIT_WAKEUP, WakeupEventDto.builder()
                                        .accountId(taskEntity.getAccountId())
                                        .mongId(taskEntity.getMongId())
                                        .build());
                    }
                }, () -> kafkaService.generateEventWithProfile("commit.test", Map.of("taskId", taskEntity.getTaskId())));
    }
}
