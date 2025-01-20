package com.monglife.mongs.app.activity.training.service;

import com.monglife.mongs.app.activity.training.config.TrainingProperties;
import com.monglife.mongs.client.manager.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingProperties trainingProperties;

    private final ManagementService managementService;

    /**
     * 몽 훈련 처리
     * 불가능 상태 : 죽음, 알
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param score 점수
     */
    @Transactional
    public void trainingRunner(Long accountId, Long mongId, Integer score) {

        managementService.patchMongAfterTraining(
                mongId,
                trainingProperties.runner.exp,
                trainingProperties.runner.weight,
                trainingProperties.runner.strength,
                trainingProperties.runner.satiety,
                trainingProperties.runner.healthy,
                trainingProperties.runner.fatigue,
                trainingProperties.runner.poopCount,
                trainingProperties.runner.payPoint * score
        );
    }
}
