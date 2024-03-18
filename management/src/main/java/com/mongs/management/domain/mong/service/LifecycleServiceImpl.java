package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.client.LifecycleClient;
import com.mongs.management.domain.mong.client.dto.response.*;
import com.mongs.management.domain.mong.controller.dto.response.DeleteMongResDto;
import com.mongs.management.domain.mong.controller.dto.response.EvolutionMongResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LifecycleServiceImpl implements LifecycleService {

    private final LifecycleClient lifecycleClient;

    @Override
    @Transactional
    public EggMongEventResDto eggMongEvent(Long mongId) {
        return lifecycleClient.eggMongEvent(mongId).getBody();
    }

    @Override
    public DeadMongEventResDto deadMongEvent(Long mongId) {
        return null;
    }

    @Override
    public SleepMongEventResDto sleepMongEvent(Long mongId, Boolean isSleeping) {
        return null;
    }

    @Override
    public GraduationMongEventResDto graduateMongEvent(Long mongId) {
        return null;
    }

    @Override
    public EvolutionReadyMongEventResDto evolutionReadyMongEvent(Long mongId) {
        return null;
    }

    @Override
    public EvolutionMongEventResDto evolutionMongEvent(Long mongId) {
        return null;
    }
}
