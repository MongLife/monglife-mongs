package com.mongs.management.global.moduleService;

import com.mongs.management.domain.management.client.dto.response.*;

import java.util.Optional;

public interface LifecycleService {
    Optional<EggMongEventResDto> eggMongEvent(Long mongId);
    Optional<EvolutionMongEventResDto> eggEvolutionMongEvent(Long mongId);
    Optional<GraduationMongEventResDto> graduateReadyMongEvent(Long mongId);
    Optional<SleepMongEventResDto> sleepMongEvent(Long mongId);
    Optional<WakeupMongEventResDto> wakeUpMongEvent(Long mongId);
    Optional<DeleteMongEventResDto> deleteMongEvent(Long mongId);
}
