package com.mongs.management.domain.management.service;

import com.mongs.core.enums.management.MongTraining;
import com.mongs.management.domain.management.service.vo.*;

import java.util.List;

public interface ManagementService {
    List<MongVo> findAllMong(Long accountId);
    MongVo registerMong(Long accountId, String name, String sleepStart, String sleepEnd);
    MongVo deleteMong(Long accountId, Long mongId);
    MongVo strokeMong(Long accountId, Long mongId);
    MongVo sleepingMong(Long accountId, Long mongId);
    MongVo poopClean(Long accountId, Long mongId);
    MongVo trainingMong(Long accountId, Long mongId, MongTraining mongTraining);
    MongVo graduateMong(Long accountId, Long mongId);
    MongVo evolutionMong(Long accountId, Long mongId);
}
