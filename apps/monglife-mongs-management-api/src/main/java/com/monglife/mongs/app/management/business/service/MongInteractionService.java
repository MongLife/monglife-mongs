package com.monglife.mongs.app.management.business.service;

import com.monglife.mongs.app.management.business.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class MongInteractionService {
    private final Random random = new Random();

    @Transactional
    public EvolutionReadyVo evolutionReady(Long mongId) {
        return EvolutionReadyVo.builder().build();
    }

//    @ValidationEvolution
    @Transactional
    public StrokeMongVo strokeMong(Long accountId, Long mongId) {
        return StrokeMongVo.builder().build();
    }

    @Transactional
    public SleepingMongVo sleepingMong(Long accountId, Long mongId) {
        return SleepingMongVo.builder().build();
    }

//    @ValidationEvolution
    @Transactional
    public PoopCleanMongVo poopClean(Long accountId, Long mongId) {
        return PoopCleanMongVo.builder().build();
    }

//    @ValidationEvolution
//    @ValidationDead
    @Transactional
    public TrainingMongVo trainingMong(Long accountId, Long mongId, String trainingCode, Integer score) {
        return TrainingMongVo.builder().build();
    }

    @Transactional
    public GraduateMongVo graduateMong(Long accountId, Long mongId) {
        return GraduateMongVo.builder().build();
    }

    @Transactional
    public EvolutionMongVo evolutionMong(Long accountId, Long mongId) {
        return EvolutionMongVo.builder().build();
    }

//    @ValidationEvolution
//    @ValidationDead
    @Transactional
    public FeedMongVo feedMong(Long accountId, Long mongId, String foodCode) {
        return FeedMongVo.builder().build();
    }

    @Transactional
    public List<FindFeedLogVo> findFeedLog(Long accountId, Long mongId) {
        return new ArrayList<>();
    }
}
