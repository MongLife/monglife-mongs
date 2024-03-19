package com.mongs.management.domain.mong.service.componentService;

import com.mongs.management.domain.mong.controller.dto.response.*;

import java.util.List;

public interface ManagementService {
    List<FindMongResDto> findAllMong(Long accountId);
    RegisterMongResDto registerMong(Long accountId, String name, String sleepStart, String sleepEnd);
    DeleteMongResDto deleteMong(Long accountId, Long mongId);
    StrokeMongResDto strokeMong(Long accountId, Long mongId);
    FeedMongResDto feedMong(Long accountId, Long mongId, String feedCode);
    SleepMongResDto sleepMong(Long accountId, Long mongId);
    PoopCleanResDto poopClean(Long accountId, Long mongId);
    TrainingMongResDto trainingMong(Long accountId, Long mongId);
    GraduateMongResDto graduateMong(Long accountId, Long mongId);
    EvolutionMongResDto evolutionMong(Long accountId, Long mongId);
}
