package com.mongs.play.app.management.internal.service;

import com.mongs.play.app.management.internal.dto.res.EvolutionReadyResDto;
import com.mongs.play.app.management.internal.vo.EvolutionReadyVo;
import com.mongs.play.app.management.internal.vo.*;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.client.publisher.mong.service.MqttService;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.service.MongStatusService;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongVo;
import com.mongs.play.module.kafka.event.managementInternal.*;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagementInternalService {

    private final MongService mongService;
    private final MongStatusService mongStatusService;
    private final KafkaService kafkaService;
    private final MqttService mqttService;

    @Transactional
    public EvolutionReadyVo evolutionReady(Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (MongUtil.isEvolutionReady(mongVo.grade(), mongVo.shift(), mongVo.exp())) {
            mongVo = mongService.toggleEvolutionReady(mongId);

            kafkaService.sendCommit(KafkaService.KafkaTopic.EVOLUTION_READY, EvolutionReadyMongEvent.builder()
                    .mongId(mongVo.mongId())
                    .build());
        }

        return EvolutionReadyVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .shift(mongVo.shift())
                .build();
    }

    @Transactional
    public DecreaseStatusVo decreaseStatus(Long mongId, Double subWeight, Double subStrength, Double subSatiety, Double subHealthy, Double subSleep) {

        MongVo mongVo = mongStatusService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);

        kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_STATUS, DecreaseStatusEvent.builder()
                .mongId(mongVo.mongId())
                .weight(mongVo.weight())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .build());

        return DecreaseStatusVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .weight(mongVo.weight())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .build();
    }

    @Transactional
    public IncreasePoopCountVo increasePoopCount(Long mongId, Integer addPoopCount) {

        MongVo mongVo = mongStatusService.increasePoopCount(mongId, addPoopCount);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_POOP_COUNT, IncreasePoopCountEvent.builder()
                .mongId(mongVo.mongId())
                .addPoopCount(addPoopCount)
                .build());

        return IncreasePoopCountVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .poopCount(mongVo.poopCount())
                .build();
    }

    @Transactional
    public IncreaseStatusVo increaseStatus(Long mongId, Double addWeight, Double addStrength, Double addSatiety, Double addHealthy, Double addSleep) {

        MongVo mongVo = mongStatusService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_STATUS, IncreaseStatusEvent.builder()
                .mongId(mongVo.mongId())
                .addWeight(addWeight)
                .addStrength(addStrength)
                .addSatiety(addSatiety)
                .addHealthy(addHealthy)
                .addSleep(addSleep)
                .weight(mongVo.weight())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .build());

        return IncreaseStatusVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .weight(mongVo.weight())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .build();
    }

    @Transactional
    public DeadMongVo dead(Long mongId) {
        MongVo mongVo = mongService.deadMong(mongId);

        return DeadMongVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .grade(mongVo.grade())
                .shift(mongVo.shift())
                .state(mongVo.state())
                .exp(mongVo.exp())
                .weight(mongVo.weight())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .poopCount(mongVo.poopCount())
                .isSleeping(mongVo.isSleeping())
                .build();
    }

    @Transactional
    public IncreasePayPointVo increasePayPoint(Long mongId, Integer addPayPoint) {
        MongVo mongVo = mongService.increasePayPoint(mongId, addPayPoint);

        return IncreasePayPointVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .payPoint(mongVo.payPoint())
                .build();
    }
}
