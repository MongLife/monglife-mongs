package com.mongs.management.domain.mong.service.componentService;

import com.mongs.management.domain.mong.controller.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface ManagementService {
    List<FindMongResDto> findAllMongAndCheckAttendance(Long accountId);
    RegisterMongResDto registerMong(Long accountId, String name, String sleepStart, String sleepEnd);
    DeleteMongResDto deleteMong(Long accountId, Long mongId);
    /*
    * 증가 요소 : exp
    * 필요 함수 : 진화 체크
    */
    StrokeMongResDto strokeMong(Long accountId, Long mongId);
    /*
     * 증가 요소 : weight, strength, satiety, healthy, sleep, exp
     * 필요 함수 : 상태 체크, 진화 체크
     */
    FeedMongResDto feedMong(Long accountId, Long mongId, String feedCode);
    SleepMongResDto sleepingMong(Long accountId, Long mongId);
    /*
     * 증가 요소 : exp
     * 필요 함수 : 진화 체크
     */
    PoopCleanResDto poopClean(Long accountId, Long mongId);
    /*
     * 증가 요소 : strength, exp
     * 필요 함수 : 상태 체크, 진화 체크
     */
    TrainingMongResDto trainingMong(Long accountId, Long mongId);
    GraduateMongResDto graduateMong(Long accountId, Long mongId);
    EvolutionMongResDto evolutionMong(Long accountId, Long mongId);
}
