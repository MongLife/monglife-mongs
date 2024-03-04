package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.service.dto.*;

import java.util.List;

public interface MongService {

    CreateMong createMong (InitMong initmong, Long memberId);
    Stroke toMongStroke (Long memberId);
    Sleep toCheckMongsLifetime (Long memberId);
    Poop toCleanMongsPoop (Long memberId);

    // 음식 먹이기
    EatTheFeed feedToMong (FeedCode feed, Long memberId);
    Training mongTraining (TrainingCount trainingCount, Long memberId);
    Evolution mongEvolution (Long memberId);
    Graduation mongsGraduate(Long memberId);
    List<SlotList> slotInfo (Long memberId); // kafka 필요, join



}
