package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.client.LifecycleClient;
import com.mongs.management.domain.mong.client.dto.response.EggMongEventResDto;
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
}
