package com.mongs.play.app.management.internal.service;

import com.mongs.play.app.management.internal.vo.*;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.service.MongStatusService;
import com.mongs.play.module.kafka.event.managementInternal.*;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagementInternalService {

    private final MongService mongService;
    private final MongStatusService mongStatusService;
    private final KafkaService kafkaService;

    public EvolutionReadyMongVo evolutionReadyMong(Long mongId) {

        Mong mong = mongService.evolutionReadyMong(mongId).mong();

        kafkaService.sendCommit(KafkaService.KafkaTopic.EVOLUTION_READY_MONG, EvolutionReadyMongEvent.builder()
                .mongId(mong.getId())
                .build());

        return EvolutionReadyMongVo.builder()
                .mongId(mong.getId())
                .shift(mong.getShift())
                .build();
    }

    public DecreaseWeightVo decreaseWeight(Long mongId, Double subWeight) {

        Mong mong = mongStatusService.decreaseWeight(mongId, subWeight);

        kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_WEIGHT, DecreaseWeightEvent.builder()
                .mongId(mong.getId())
                .subWeight(subWeight)
                .build());

        return DecreaseWeightVo.builder()
                .mongId(mong.getId())
                .weight(mong.getWeight())
                .build();
    }

    public DecreaseStrengthVo decreaseStrength(Long mongId, Double subStrength) {

        Mong mong = mongStatusService.decreaseStrength(mongId, subStrength);

        kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_STRENGTH, DecreaseStrengthEvent.builder()
                .mongId(mong.getId())
                .subStrength(subStrength)
                .build());

        return DecreaseStrengthVo.builder()
                .mongId(mong.getId())
                .strength(mong.getStrength())
                .build();
    }

    public DecreaseSatietyVo decreaseSatiety(Long mongId, Double subSatiety) {

        Mong mong = mongStatusService.decreaseSatiety(mongId, subSatiety);

        kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_SATIETY, DecreaseSatietyEvent.builder()
                .mongId(mong.getId())
                .subSatiety(subSatiety)
                .build());

        return DecreaseSatietyVo.builder()
                .mongId(mong.getId())
                .satiety(mong.getSatiety())
                .build();
    }

    public DecreaseHealthyVo decreaseHealthy(Long mongId, Double subHealthy) {

        Mong mong = mongStatusService.decreaseHealthy(mongId, subHealthy);

        kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_HEALTHY, DecreaseHealthyEvent.builder()
                .mongId(mong.getId())
                .subHealthy(subHealthy)
                .build());

        return DecreaseHealthyVo.builder()
                .mongId(mong.getId())
                .healthy(mong.getHealthy())
                .build();
    }

    public DecreaseSleepVo decreaseSleep(Long mongId, Double subSleep) {

        Mong mong = mongStatusService.decreaseSleep(mongId, subSleep);

        kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_SLEEP, DecreaseSleepEvent.builder()
                .mongId(mong.getId())
                .subSleep(subSleep)
                .build());

        return DecreaseSleepVo.builder()
                .mongId(mong.getId())
                .sleep(mong.getSleep())
                .build();
    }

    public DecreasePoopCountVo decreasePoopCount(Long mongId, Integer subPoopCount) {

        Mong mong = mongStatusService.decreasePoopCount(mongId, subPoopCount);

        kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_POOP_COUNT, DecreasePoopCountEvent.builder()
                .mongId(mong.getId())
                .subPoopCount(subPoopCount)
                .build());

        return DecreasePoopCountVo.builder()
                .mongId(mong.getId())
                .poopCount(mong.getPoopCount())
                .build();
    }

    public IncreaseWeightVo increaseWeight(Long mongId, Double addWeight) {

        Mong mong = mongStatusService.increaseWeight(mongId, addWeight);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_WEIGHT, IncreaseWeightEvent.builder()
                .mongId(mong.getId())
                .addWeight(addWeight)
                .build());

        return IncreaseWeightVo.builder()
                .mongId(mong.getId())
                .weight(mong.getWeight())
                .build();
    }

    public IncreaseStrengthVo increaseStrength(Long mongId, Double addStrength) {

        Mong mong = mongStatusService.increaseStrength(mongId, addStrength);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_STRENGTH, IncreaseStrengthEvent.builder()
                .mongId(mong.getId())
                .addStrength(addStrength)
                .build());

        return IncreaseStrengthVo.builder()
                .mongId(mong.getId())
                .strength(mong.getStrength())
                .build();
    }

    public IncreaseSatietyVo increaseSatiety(Long mongId, Double addSatiety) {

        Mong mong = mongStatusService.increaseSatiety(mongId, addSatiety);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_SATIETY, IncreaseSatietyEvent.builder()
                .mongId(mong.getId())
                .addSatiety(addSatiety)
                .build());

        return IncreaseSatietyVo.builder()
                .mongId(mong.getId())
                .satiety(mong.getSatiety())
                .build();
    }

    public IncreaseHealthyVo increaseHealthy(Long mongId, Double addHealthy) {

        Mong mong = mongStatusService.increaseHealthy(mongId, addHealthy);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_HEALTHY, IncreaseHealthyEvent.builder()
                .mongId(mong.getId())
                .addHealthy(addHealthy)
                .build());

        return IncreaseHealthyVo.builder()
                .mongId(mong.getId())
                .healthy(mong.getHealthy())
                .build();
    }

    public IncreaseSleepVo increaseSleep(Long mongId, Double addSleep) {

        Mong mong = mongStatusService.increaseSleep(mongId, addSleep);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_SLEEP, IncreaseSleepEvent.builder()
                .mongId(mong.getId())
                .addSleep(addSleep)
                .build());

        return IncreaseSleepVo.builder()
                .mongId(mong.getId())
                .sleep(mong.getSleep())
                .build();
    }

    public IncreasePoopCountVo increasePoopCount(Long mongId, Integer addPoopCount) {

        Mong mong = mongStatusService.increasePoopCount(mongId, addPoopCount);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_POOP_COUNT, IncreasePoopCountEvent.builder()
                .mongId(mong.getId())
                .addPoopCount(addPoopCount)
                .build());

        return IncreasePoopCountVo.builder()
                .mongId(mong.getId())
                .poopCount(mong.getPoopCount())
                .build();
    }

    public DeadMongVo deadMong(Long mongId) {

        Mong mong = mongService.deadMong(mongId).mong();

        kafkaService.sendCommit(KafkaService.KafkaTopic.DEAD_MONG, DeadMongEvent.builder()
                .mongId(mong.getId())
                .build());

        return DeadMongVo.builder()
                .mongId(mong.getId())
                .shift(mong.getShift())
                .build();
    }
}
