package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.client.dto.response.EggMongEventResDto;

public interface LifecycleService {
    EggMongEventResDto eggMongEvent(Long mongId);
}
