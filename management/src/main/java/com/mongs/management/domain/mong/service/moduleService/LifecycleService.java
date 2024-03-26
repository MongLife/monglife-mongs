package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.management.domain.mong.client.dto.response.*;

import java.util.Optional;

public interface LifecycleService {
    Optional<EggMongEventResDto> eggMongEvent(Long mongId);
    Optional<DeleteMongEventResDto> deleteMongEvent(Long mongId);
    Optional<SleepMongEventResDto> sleepMongEvent(Long mongId);
    Optional<WakeupMongEventResDto> wakeUpMongEvent(Long mongId);
    Optional<GraduationMongEventResDto> graduateReadyMongEvent(Long mongId);
    Optional<EvolutionMongEventResDto> eggEvolutionMongEvent(Long mongId);
}
