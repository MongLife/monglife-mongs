package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.management.domain.mong.client.LifecycleClient;
import com.mongs.management.domain.mong.client.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LifecycleServiceImpl implements LifecycleService {

    private final LifecycleClient lifecycleClient;

    @Override
    public EggMongEventResDto eggMongEvent(Long mongId) {
        return lifecycleClient.eggMongEvent(mongId).getBody();
    }

    @Override
    public DeadMongEventResDto deadMongEvent(Long mongId) {
        return lifecycleClient.deadMongEvent(mongId).getBody();
    }

    @Override
    public SleepMongEventResDto sleepMongEvent(Long mongId) {
        return lifecycleClient.sleepMongEvent(mongId).getBody();
    }

    public WakeupMongEventResDto wakeUpMongEvent(Long mongId) {
        return lifecycleClient.wakeupMongEvent(mongId).getBody();
    }

    @Override
    public GraduationMongEventResDto graduateMongEvent(Long mongId) {
        return lifecycleClient.graduationMongEvent(mongId).getBody();
    }

    @Override
    public EvolutionReadyMongEventResDto evolutionReadyMongEvent(Long mongId) {
        return lifecycleClient.evolutionReadyMongEvent(mongId).getBody();
    }

    @Override
    public EvolutionMongEventResDto evolutionMongEvent(Long mongId) {
        return lifecycleClient.evolutionMongEvent(mongId).getBody();
    }
}
