package com.monglife.mongs.app.activity.training.service;

import com.monglife.mongs.app.activity.training.config.TrainingProperties;
import com.monglife.mongs.domain.mong.annotation.MongAccountCheck;
import com.monglife.mongs.domain.mong.dto.etc.PatchMongStatusDto;
import com.monglife.mongs.domain.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final MongService mongService;

    private final TrainingProperties trainingProperties;

    @MongAccountCheck
    @Transactional
    public void trainingRunner(Long accountId, Long mongId, Integer score) {

        PatchMongStatusDto patchMongStatusDto = PatchMongStatusDto.builder()
                .exp(trainingProperties.runner.exp)
                .weight(trainingProperties.runner.weight)
                .strength(trainingProperties.runner.strength)
                .satiety(trainingProperties.runner.satiety)
                .healthy(trainingProperties.runner.healthy)
                .fatigue(trainingProperties.runner.fatigue)
                .poopCount(trainingProperties.runner.poopCount)
                .payPoint(trainingProperties.runner.payPoint * score)
                .build();

        mongService.trainingMong(mongId, patchMongStatusDto);
    }
}
