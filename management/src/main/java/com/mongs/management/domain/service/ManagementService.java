package com.mongs.management.domain.service;

import com.mongs.management.domain.service.dto.*;

public interface ManagementService {

    CreateMong createMong (InitMong initmong, Long memberId);
    Stroke toMongStroke (Long memberId);
    Sleep toCheckMongsLifetime (Long memberId);
    Poop toCleanMongsPoop (Long memberId);
    EatTheFeed feedToMong (FeedCode feedCode, Long memberId);
    Training mongTraining (TrainingCount trainingCount, Long memberId);
    Evolution mongEvolution (Long memberId);
    Graduation mongsGraduate(Long memberId);
}
