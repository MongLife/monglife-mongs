package com.mongs.management.domain.mong.service.componentService;

import com.mongs.core.enums.management.MongTraining;
import com.mongs.management.domain.mong.service.componentService.vo.*;
import com.mongs.management.domain.mong.service.vo.*;

import java.util.List;

public interface ManagementService {
    List<MongVo> findAllMong(Long accountId);
    MongVo registerMong(Long accountId, String name, String sleepStart, String sleepEnd);
    MongVo deleteMong(Long accountId, Long mongId);
    MongVo strokeMong(Long accountId, Long mongId);
    MongVo feedMong(Long accountId, Long mongId, String foodCode);
    MongVo sleepingMong(Long accountId, Long mongId);
    /*
     * 증가 요소 : exp
     * 필요 함수 : 진화 체크
     */
    MongVo poopClean(Long accountId, Long mongId);
    /*
     * 증가 요소 : strength, exp
     * 필요 함수 : 상태 체크, 진화 체크
     */
    MongVo trainingMong(Long accountId, Long mongId, MongTraining mongTraining);
    MongVo graduateMong(Long accountId, Long mongId);
    MongVo evolutionMong(Long accountId, Long mongId);
}
