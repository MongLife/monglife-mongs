package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.app.management.internal.dto.res.*;
import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.client.publisher.mong.annotation.Notification;
import com.mongs.play.client.publisher.mong.code.PublishCode;
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

    @Notification(code = PublishCode.EVOLUTION_READY)
    @KafkaListener(topics = { "commit.zeroEvolutionSchedule" })
    public EvolutionReadyResDto evolutionReady(ZeroEvolutionScheduleEvent payload) {

        EvolutionReadyResDto evolutionReadyResDto = null;

        try {
            Long mongId = payload.getMongId();

            var vo = managementInternalService.evolutionReady(mongId);
            evolutionReadyResDto = EvolutionReadyResDto.builder()
                    .accountId(vo.accountId())
                    .mongId(vo.mongId())
                    .shiftCode(vo.shift().code)
                    .build();
        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.ZERO_EVOLUTION_SCHEDULE, payload);
        }

        return evolutionReadyResDto;
    }

    @Notification(code = PublishCode.DECREASE_STATUS)
    @KafkaListener(topics = { "commit.decreaseStatusSchedule" })
    public DecreaseStatusResDto decreaseStatus(DecreaseStatusScheduleEvent payload) {

        DecreaseStatusResDto decreaseStatusResDto = null;

        try {
            Long mongId = payload.getMongId();
            Double subWeight = payload.getSubWeight();
            Double subStrength = payload.getSubStrength();
            Double subSatiety = payload.getSubSatiety();
            Double subHealthy = payload.getSubHealthy();
            Double subSleep = payload.getSubSleep();

            var vo = managementInternalService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);
            decreaseStatusResDto = DecreaseStatusResDto.builder()
                    .accountId(vo.accountId())
                    .mongId(vo.mongId())
                    .weight(vo.weight())
                    .strength(vo.strength())
                    .satiety(vo.satiety())
                    .healthy(vo.healthy())
                    .sleep(vo.sleep())
                    .build();

        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.DECREASE_STATUS_SCHEDULE, payload);
        } catch (CommonErrorException e) {
            if (e.errorCode.equals(MongErrorCode.NOT_FOUND_ACTIVE_MONG)) {
                kafkaService.sendStop(KafkaService.KafkaTopic.DECREASE_STATUS_SCHEDULE, payload);
            }
        }

        return decreaseStatusResDto;
    }

    @Notification(code = PublishCode.INCREASE_STATUS)
    @KafkaListener(topics = { "commit.increaseStatusSchedule" })
    public IncreaseStatusResDto increaseStatus(IncreaseStatusScheduleEvent payload) {

        IncreaseStatusResDto increaseStatusResDto = null;

        try {
            Long mongId = payload.getMongId();
            Double addWeight = payload.getAddWeight();
            Double addStrength = payload.getAddStrength();
            Double addSatiety = payload.getAddSatiety();
            Double addHealthy = payload.getAddHealthy();
            Double addSleep = payload.getAddSleep();

            var vo = managementInternalService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);
            increaseStatusResDto = IncreaseStatusResDto.builder()
                    .accountId(vo.accountId())
                    .mongId(vo.mongId())
                    .weight(vo.weight())
                    .strength(vo.strength())
                    .satiety(vo.satiety())
                    .healthy(vo.healthy())
                    .sleep(vo.sleep())
                    .build();

        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.INCREASE_STATUS_SCHEDULE, payload);
        } catch (CommonErrorException e) {
            if (e.errorCode.equals(MongErrorCode.NOT_FOUND_ACTIVE_MONG)) {
                kafkaService.sendStop(KafkaService.KafkaTopic.INCREASE_STATUS_SCHEDULE, payload);
            }
        }

        return increaseStatusResDto;
    }

    @Notification(code = PublishCode.INCREASE_POOP_COUNT)
    @KafkaListener(topics = { "commit.increasePoopCountSchedule" })
    public IncreasePoopCountResDto increasePoopCount(IncreasePoopCountScheduleEvent payload) {

        IncreasePoopCountResDto increasePoopCountResDto = null;

        try {
            Long mongId = payload.getMongId();
            Integer addPoopCount = payload.getAddPoopCount();

            var vo = managementInternalService.increasePoopCount(mongId, addPoopCount);
            increasePoopCountResDto = IncreasePoopCountResDto.builder()
                    .accountId(vo.accountId())
                    .mongId(vo.mongId())
                    .poopCount(vo.poopCount())
                    .build();
        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.INCREASE_POOP_COUNT_SCHEDULE, payload);
        } catch (CommonErrorException e) {
            if (e.errorCode.equals(MongErrorCode.NOT_FOUND_ACTIVE_MONG)) {
                kafkaService.sendStop(KafkaService.KafkaTopic.INCREASE_POOP_COUNT_SCHEDULE, payload);
            }
        }

        return increasePoopCountResDto;
    }

    @Notification(code = PublishCode.DEAD)
    @KafkaListener(topics = { "commit.deadSchedule" })
    public DeadMongResDto deadMong(DeadScheduleEvent payload) {

        DeadMongResDto deadMongResDto = null;

        try {
            Long mongId = payload.getMongId();

            var vo = managementInternalService.dead(mongId);
            deadMongResDto = DeadMongResDto.builder()
                    .accountId(vo.accountId())
                    .mongId(vo.mongId())
                    .grade(vo.grade())
                    .shift(vo.shift())
                    .state(vo.state())
                    .exp(vo.exp())
                    .weight(vo.weight())
                    .strength(vo.strength())
                    .satiety(vo.satiety())
                    .healthy(vo.healthy())
                    .sleep(vo.sleep())
                    .poopCount(vo.poopCount())
                    .isSleeping(vo.isSleeping())
                    .build();
        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.DEAD_SCHEDULE, payload);
        }

        return deadMongResDto;
    }

    @Notification(code = PublishCode.EXCHANGE_PAY_POINT)
    @KafkaListener(topics = { "commit.exchangePayPoint" })
    public ExchangePayPointResDto exchangePayPoint(ExchangePayPointEvent payload) {

        ExchangePayPointResDto exchangePayPointResDto = null;

        try {
            Long mongId = payload.getMongId();
            Integer addPayPoint = payload.getAddPayPoint();

            var vo = managementInternalService.increasePayPoint(mongId, addPayPoint);
            exchangePayPointResDto = ExchangePayPointResDto.builder()
                    .accountId(vo.accountId())
                    .mongId(vo.mongId())
                    .payPoint(vo.payPoint())
                    .build();
        } catch (AppErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.EXCHANGE_PAY_POINT, payload);
        } catch (CommonErrorException e) {
            kafkaService.sendRollback(KafkaService.KafkaTopic.EXCHANGE_PAY_POINT, payload);
        }

        return exchangePayPointResDto;
    }
}
