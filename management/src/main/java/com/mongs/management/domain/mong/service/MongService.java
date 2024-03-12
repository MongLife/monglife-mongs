package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.service.dto.*;

import java.util.List;

public interface MongService {

    CreateMong createMong (InitMong initmong, Long accountId, String email);
    Stroke toMongStroke (Long mongId, Long accountId);
    Sleep toMongSleeping (Long mongId, Long accountId, String email);
    Poop toCleanMongsPoop (Long mongId, Long accountId, String email);

    // 음식 먹이기
    EatTheFeed feedToMong (FeedCode code, Long mongId, Long accountId, String email);
    Training mongTraining (Long mongId, Long accountId);
    Evolution mongEvolution (Long mongId, Long accountId, String email);
    Graduation mongsGraduate(Long mongId, Long accountId, String email);
}
