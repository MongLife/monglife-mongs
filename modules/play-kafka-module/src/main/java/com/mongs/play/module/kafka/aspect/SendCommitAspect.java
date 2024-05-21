package com.mongs.play.module.kafka.aspect;

import com.mongs.play.module.kafka.annotation.SendCommit;
import com.mongs.play.module.kafka.event.managementExternal.DeleteMongEvent;
import com.mongs.play.module.kafka.event.managementExternal.EvolutionMongEvent;
import com.mongs.play.module.kafka.event.managementExternal.RegisterMongEvent;
import com.mongs.play.module.kafka.event.managementExternal.SleepingMongEvent;
import com.mongs.play.module.kafka.event.managementInternal.*;
import com.mongs.play.module.kafka.event.managementWorker.*;
import com.mongs.play.module.kafka.event.playerExternal.ExchangePayPointEvent;
import com.mongs.play.module.kafka.event.playerInternal.RegisterMapCollectionEvent;
import com.mongs.play.module.kafka.event.playerInternal.RegisterMongCollectionEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

@Slf4j
@Order(3)
@Aspect
@Component
@RequiredArgsConstructor
public class SendCommitAspect {

    private final KafkaService kafkaService;

    @Around(value = "execution(* com.mongs.play..*.*(..)) && @annotation(annotation)")
    public Object sendCommit(ProceedingJoinPoint joinPoint, SendCommit annotation) throws Throwable {
        Object returnValue = joinPoint.proceed();

        Object body = returnValue;

        log.info("sendCommit execute");

        if (returnValue instanceof ResponseEntity<?>) {
            body = ((ResponseEntity<?>) returnValue).getBody();
        }

        if (!ObjectUtils.isEmpty(body)) {

            String commitTopic = annotation.topic();

            switch (commitTopic) {
                case KafkaService.CommitTopic.REGISTER_MONG -> {
                    RegisterMongEvent event = (RegisterMongEvent) this.setFields(body, new RegisterMongEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.FIRST_EVOLUTION_MONG, KafkaService.CommitTopic.EVOLUTION_MONG, KafkaService.CommitTopic.LAST_EVOLUTION_MONG -> {
                    EvolutionMongEvent event = (EvolutionMongEvent) this.setFields(body, new EvolutionMongEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.TRAINING_MONG -> {
                    TrainingMongEvent event = (TrainingMongEvent) this.setFields(body, new TrainingMongEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.FEED_MONG -> {
                    FeedMongEvent event = (FeedMongEvent) this.setFields(body, new FeedMongEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.SLEEPING_MONG -> {
                    SleepingMongEvent event = (SleepingMongEvent) this.setFields(body, new SleepingMongEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.DELETE_MONG -> {
                    DeleteMongEvent event = (DeleteMongEvent) this.setFields(body, new DeleteMongEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.EVOLUTION_READY -> {
                    EvolutionReadyMongEvent event = (EvolutionReadyMongEvent) this.setFields(body, new EvolutionReadyMongEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.DECREASE_STATUS -> {
                    DecreaseStatusEvent event = (DecreaseStatusEvent) this.setFields(body, new DecreaseStatusEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.INCREASE_STATUS -> {
                    IncreaseStatusEvent event = (IncreaseStatusEvent) this.setFields(body, new IncreaseStatusEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.INCREASE_POOP_COUNT -> {
                    IncreasePoopCountEvent event = (IncreasePoopCountEvent) this.setFields(body, new IncreasePoopCountEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.ZERO_EVOLUTION_SCHEDULE -> {
                    ZeroEvolutionScheduleEvent event = (ZeroEvolutionScheduleEvent) this.setFields(body, new ZeroEvolutionScheduleEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.DECREASE_STATUS_SCHEDULE -> {
                    DecreaseStatusScheduleEvent event = (DecreaseStatusScheduleEvent) this.setFields(body, new DecreaseStatusScheduleEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.INCREASE_STATUS_SCHEDULE -> {
                    IncreaseStatusScheduleEvent event = (IncreaseStatusScheduleEvent) this.setFields(body, new IncreaseStatusScheduleEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.INCREASE_POOP_COUNT_SCHEDULE -> {
                    IncreasePoopCountScheduleEvent event = (IncreasePoopCountScheduleEvent) this.setFields(body, new IncreasePoopCountScheduleEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.DEAD_SCHEDULE -> {
                    DeadScheduleEvent event = (DeadScheduleEvent) this.setFields(body, new DeadScheduleEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.REGISTER_MAP_COLLECTION -> {
                    RegisterMapCollectionEvent event = (RegisterMapCollectionEvent) this.setFields(body, new RegisterMapCollectionEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.REGISTER_MONG_COLLECTION -> {
                    RegisterMongCollectionEvent event = (RegisterMongCollectionEvent) this.setFields(body, new RegisterMongCollectionEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                case KafkaService.CommitTopic.EXCHANGE_PAY_POINT -> {
                    ExchangePayPointEvent event = (ExchangePayPointEvent) this.setFields(body, new ExchangePayPointEvent());
                    kafkaService.sendCommit(commitTopic, event);
                }
                default -> {}
            }
        }

        return returnValue;
    }

    private Object setFields(Object body, Object publishVo) {
        Field[] publishVoFields = publishVo.getClass().getDeclaredFields();
        Field[] bodyFields = body.getClass().getDeclaredFields();

        for (Field publishVoField : publishVoFields) {
            for (Field bodyField : bodyFields) {

                String publishVoFieldName = publishVoField.getName();
                String bodyFieldName = bodyField.getName();

                if (publishVoFieldName.equals(bodyFieldName)) {
                    try {
                        boolean isBodyFieldAccessible = bodyField.canAccess(body);
                        boolean isPublishVoFieldAccessible = publishVoField.canAccess(publishVo);

                        bodyField.setAccessible(true);
                        publishVoField.setAccessible(true);

                        Object bodyFieldValue = bodyField.get(body);
                        publishVoField.set(publishVo, bodyFieldValue);

                        bodyField.setAccessible(isBodyFieldAccessible);
                        publishVoField.setAccessible(isPublishVoFieldAccessible);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }


                    break;
                }
            }
        }

        return publishVo;
    }
}