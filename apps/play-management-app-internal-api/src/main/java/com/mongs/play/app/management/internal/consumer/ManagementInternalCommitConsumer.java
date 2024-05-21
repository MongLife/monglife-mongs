package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.app.AppErrorException;
import com.mongs.play.core.exception.common.CommonErrorException;
import com.mongs.play.module.kafka.event.managementWorker.*;
import com.mongs.play.module.kafka.event.playerExternal.ExchangePayPointEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalCommitConsumer {

    private final KafkaService kafkaService;
    private final ManagementInternalService managementInternalService;

    @KafkaListener(topics = { KafkaService.CommitTopic.ZERO_EVOLUTION_SCHEDULE })
    public void evolutionReady(ZeroEvolutionScheduleEvent payload) {
        try {
            Long mongId = payload.getMongId();

            var vo = managementInternalService.evolutionReady(mongId);

        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.ZERO_EVOLUTION_SCHEDULE, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.DECREASE_STATUS_SCHEDULE })
    public void decreaseStatus(DecreaseStatusScheduleEvent payload) {

        try {
            Long mongId = payload.getMongId();
            Double subWeight = payload.getSubWeight();
            Double subStrength = payload.getSubStrength();
            Double subSatiety = payload.getSubSatiety();
            Double subHealthy = payload.getSubHealthy();
            Double subSleep = payload.getSubSleep();

            var vo = managementInternalService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);

        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.DECREASE_STATUS_SCHEDULE, payload);
        } catch (CommonErrorException e) {
            if (e.errorCode.equals(MongErrorCode.NOT_FOUND_ACTIVE_MONG)) {
                kafkaService.sendCommit(KafkaService.StopTopic.DECREASE_STATUS_SCHEDULE, payload);
            }
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.INCREASE_STATUS_SCHEDULE })
    public void increaseStatus(IncreaseStatusScheduleEvent payload) {
        try {
            Long mongId = payload.getMongId();
            Double addWeight = payload.getAddWeight();
            Double addStrength = payload.getAddStrength();
            Double addSatiety = payload.getAddSatiety();
            Double addHealthy = payload.getAddHealthy();
            Double addSleep = payload.getAddSleep();

            var vo = managementInternalService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);

        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.INCREASE_STATUS_SCHEDULE, payload);
        } catch (CommonErrorException e) {
            if (e.errorCode.equals(MongErrorCode.NOT_FOUND_ACTIVE_MONG)) {
                kafkaService.sendCommit(KafkaService.StopTopic.INCREASE_STATUS_SCHEDULE, payload);
            }
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.INCREASE_POOP_COUNT_SCHEDULE })
    public void increasePoopCount(IncreasePoopCountScheduleEvent payload) {
        try {
            Long mongId = payload.getMongId();
            Integer addPoopCount = payload.getAddPoopCount();

            var vo = managementInternalService.increasePoopCount(mongId, addPoopCount);
        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.INCREASE_POOP_COUNT_SCHEDULE, payload);
        } catch (CommonErrorException e) {
            if (e.errorCode.equals(MongErrorCode.NOT_FOUND_ACTIVE_MONG)) {
                kafkaService.sendCommit(KafkaService.StopTopic.INCREASE_POOP_COUNT_SCHEDULE, payload);
            }
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.DEAD_SCHEDULE })
    public void deadMong(DeadScheduleEvent payload) {
        try {
            Long mongId = payload.getMongId();

            var vo = managementInternalService.dead(mongId);

        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.DEAD_SCHEDULE, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.EXCHANGE_PAY_POINT })
    public void increasePayPoint(ExchangePayPointEvent payload) {
        try {
            Long mongId = payload.getMongId();
            Integer addPayPoint = payload.getAddPayPoint();

            var vo = managementInternalService.increasePayPoint(mongId, addPayPoint);

        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.EXCHANGE_PAY_POINT, payload);
        } catch (CommonErrorException e) {
            kafkaService.sendCommit(KafkaService.StopTopic.EXCHANGE_PAY_POINT, payload);
        }
    }
}
