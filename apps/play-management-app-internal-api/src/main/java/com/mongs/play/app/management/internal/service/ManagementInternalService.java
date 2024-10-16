package com.mongs.play.app.management.internal.service;

import com.mongs.play.app.management.internal.annotation.ValidationDead;
import com.mongs.play.app.management.internal.vo.EvolutionReadyVo;
import com.mongs.play.app.management.internal.vo.*;
import com.mongs.play.domain.mong.service.MongPayPointService;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.service.MongStatusService;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagementInternalService {

    private final MongService mongService;
    private final MongPayPointService mongPayPointService;
    private final MongStatusService mongStatusService;

    @Transactional
    public EvolutionReadyVo evolutionReady(Long mongId) {

        MongVo mongVo = mongService.toggleEvolutionReady(mongId);

        return EvolutionReadyVo.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().code)
                .build();
    }

    @Transactional
    public void toggleIsDeadSchedule(Long mongId, Boolean isDeadSchedule) {
        MongVo mongVo = mongService.toggleIsDeadSchedule(mongId, isDeadSchedule);
    }

    @ValidationDead
    @Transactional
    public DecreaseStatusVo decreaseStatus(Long mongId, Double subWeight, Double subStrength, Double subSatiety, Double subHealthy, Double subSleep) {

        MongVo mongVo = mongStatusService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);
        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), mongVo);

        return DecreaseStatusVo.builder()
                .mongId(mongVo.mongId())
                .weight(mongVo.weight())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .isDeadSchedule(mongVo.isDeadSchedule())
                .stateCode(mongVo.state().code)
                .build();
    }

    @Transactional
    public IncreasePoopCountVo increasePoopCount(Long mongId, Integer addPoopCount) {

        MongVo mongVo = mongStatusService.increasePoopCount(mongId, addPoopCount);

        return IncreasePoopCountVo.builder()
                .mongId(mongVo.mongId())
                .poopCount(mongVo.poopCount())
                .build();
    }

    @ValidationDead
    @Transactional
    public IncreaseStatusVo increaseStatus(Long mongId, Double addWeight, Double addStrength, Double addSatiety, Double addHealthy, Double addSleep) {

        MongVo mongVo = mongStatusService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);
        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), mongVo);

        return IncreaseStatusVo.builder()
                .mongId(mongVo.mongId())
                .weight(mongVo.weight())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .isDeadSchedule(mongVo.isDeadSchedule())
                .stateCode(mongVo.state().code)
                .build();
    }

    @Transactional
    public DeadMongVo dead(Long mongId) {
        MongVo mongVo = mongService.deadMong(mongId);
        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), mongVo);

        return DeadMongVo.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().code)
                .stateCode(mongVo.state().code)
                .poopCount(mongVo.poopCount())
                .isSleeping(mongVo.isSleeping())
                .weight(mongVo.weight())
                .exp(mongVo.exp())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .expPercent(mongStatusPercentVo.exp())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .build();
    }


    @Transactional
    public IncreasePayPointVo increasePayPoint(Long mongId, Integer addPayPoint) {
        MongVo mongVo = mongPayPointService.increasePayPoint(mongId, addPayPoint);

        return IncreasePayPointVo.builder()
                .mongId(mongVo.mongId())
                .payPoint(mongVo.payPoint())
                .build();
    }
}
