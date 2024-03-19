package com.mongs.management.domain.mong.service.componentService;

import com.mongs.management.domain.mong.controller.dto.response.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ManagementService {
//    void checkAttendance(Long accountId);
    List<FindMongResDto> findAllMong(Long accountId);
    RegisterMongResDto registerMong(Long accountId, String name, String sleepStart, String sleepEnd);
    DeleteMongResDto deleteMong(Long accountId, Long mongId);
    StrokeMongResDto strokeMong(Long accountId, Long mongId);
    FeedMongResDto feedMong(Long accountId, Long mongId, String feedCode);
    SleepMongResDto sleepingMong(Long accountId, Long mongId);
    PoopCleanResDto poopClean(Long accountId, Long mongId);
    TrainingMongResDto trainingMong(Long accountId, Long mongId);
    GraduateMongResDto graduateMong(Long accountId, Long mongId);
    EvolutionMongResDto evolutionMong(Long accountId, Long mongId);
}
