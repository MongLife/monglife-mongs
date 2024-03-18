package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.client.dto.response.*;

public interface LifecycleService {
    // for RegisterMong
    EggMongEventResDto eggMongEvent(Long mongId);
    // for deleteMong
    DeadMongEventResDto deadMongEvent(Long mongId);
    // for sleepMong
    SleepMongEventResDto sleepMongEvent(Long mongId, Boolean isSleeping);
    // for graduateMong
    GraduationMongEventResDto graduateMongEvent(Long mongId);
    // for evolutionReadyMong
    EvolutionReadyMongEventResDto evolutionReadyMongEvent(Long mongId);
    // for evolutionMong
    EvolutionMongEventResDto evolutionMongEvent(Long mongId);
}
